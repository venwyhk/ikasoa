package com.ikasoa.core.netty;

import org.apache.thrift.TApplicationException;
import org.apache.thrift.TException;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TMessage;
import org.apache.thrift.protocol.TMessageType;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.util.Timeout;
import org.jboss.netty.util.Timer;
import org.jboss.netty.util.TimerTask;

import com.ikasoa.core.netty.server.NettyServerConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * NettyDispatcher
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.6
 */
public class NettyDispatcher extends SimpleChannelUpstreamHandler {

	private final TProcessorFactory processorFactory;
	private final Executor executor = Executors.newSingleThreadExecutor();
	private final long taskTimeoutMillis;
	private final Timer taskTimeoutTimer;
	private final long queueTimeoutMillis;
	private final short queuedResponseLimit;
	private final Map<Integer, TNettyMessage> responseMap = new HashMap<>();
	private final AtomicInteger dispatcherSequenceId = new AtomicInteger(0);
	private final AtomicInteger lastResponseWrittenId = new AtomicInteger(0);
	private final TProtocolFactory protocolFactory;

	public NettyDispatcher(NettyServerConfiguration configuration, Timer timer) {
		this.processorFactory = configuration.getProcessorFactory();
		this.protocolFactory = configuration.getProtocolFactory();
		this.queuedResponseLimit = configuration.getQueuedResponseLimit();
		this.taskTimeoutMillis = 0;
		this.taskTimeoutTimer = timer;
		this.queueTimeoutMillis = 0;
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		if (e.getMessage() instanceof TNettyMessage) {
			TNettyMessage message = (TNettyMessage) e.getMessage();
			message.setProcessStartTimeMillis(System.currentTimeMillis());
			checkResponseOrderingRequirements(ctx, message);
			TNettyTransport messageTransport = new TNettyTransport(ctx.getChannel(), message);
			TProtocol protocol = protocolFactory.getProtocol(messageTransport);
			processRequest(ctx, message, messageTransport, protocol, protocol);
		} else
			ctx.sendUpstream(e);
	}

	private void checkResponseOrderingRequirements(ChannelHandlerContext ctx, TNettyMessage message) {
		boolean messageRequiresOrderedResponses = message.isOrderedResponsesRequired();
		if (!DispatcherContext.isResponseOrderingRequirementInitialized(ctx))
			// 第一个请求
			DispatcherContext.setResponseOrderingRequired(ctx, messageRequiresOrderedResponses);
		else if (messageRequiresOrderedResponses != DispatcherContext.isResponseOrderingRequired(ctx))
			throw new IllegalStateException(
					"Every message on a single channel must specify the same requirement for response ordering .");
	}

	private void processRequest(final ChannelHandlerContext ctx, final TNettyMessage message,
			final TNettyTransport messageTransport, final TProtocol inProtocol, final TProtocol outProtocol) {

		final int requestSequenceId = dispatcherSequenceId.incrementAndGet();

		if (DispatcherContext.isResponseOrderingRequired(ctx))
			synchronized (responseMap) {
				if (requestSequenceId > lastResponseWrittenId.get() + queuedResponseLimit
						&& !DispatcherContext.isChannelReadBlocked(ctx))
					DispatcherContext.blockChannelReads(ctx);
			}

		try {
			executor.execute(() -> {
				final AtomicBoolean responseSent = new AtomicBoolean(false);
				final AtomicReference<Timeout> expireTimeout = new AtomicReference<>(null);
				try {
					long timeRemaining = 0;
					long timeElapsed = System.currentTimeMillis() - message.getProcessStartTimeMillis();
					if (queueTimeoutMillis > 0)
						if (timeElapsed >= queueTimeoutMillis) {
							sendTApplicationException(
									new TApplicationException(TApplicationException.INTERNAL_ERROR, String.format(
											"Task stayed on the queue for %d milliseconds, exceeding configured queue timeout of %d milliseconds .",
											timeElapsed, queueTimeoutMillis)),
									ctx, message, requestSequenceId, messageTransport, inProtocol, outProtocol);
							return;
						} else if (taskTimeoutMillis > 0)
							if (timeElapsed >= taskTimeoutMillis) {
								sendTApplicationException(
										new TApplicationException(TApplicationException.INTERNAL_ERROR, String.format(
												"Task stayed on the queue for %d milliseconds, exceeding configured task timeout of %d milliseconds .",
												timeElapsed, taskTimeoutMillis)),
										ctx, message, requestSequenceId, messageTransport, inProtocol, outProtocol);
								return;
							} else
								timeRemaining = taskTimeoutMillis - timeElapsed;

					if (timeRemaining > 0)
						expireTimeout.set(taskTimeoutTimer.newTimeout(new TimerTask() {
							@Override
							public void run(Timeout timeout) throws Exception {
								if (responseSent.compareAndSet(false, true)) {
									ChannelBuffer duplicateBuffer = message.getBuffer().duplicate();
									duplicateBuffer.resetReaderIndex();
									TNettyTransport temporaryTransport = new TNettyTransport(ctx.getChannel(),
											duplicateBuffer, message.getTransportType());
									TProtocol protocol = protocolFactory.getProtocol(messageTransport);
									sendTApplicationException(
											new TApplicationException(TApplicationException.INTERNAL_ERROR,
													"Task timed out while executing ."),
											ctx, message, requestSequenceId, temporaryTransport, protocol, protocol);
								}
							}
						}, timeRemaining, TimeUnit.MILLISECONDS));

					if (processorFactory.getProcessor(messageTransport).process(inProtocol, outProtocol)
							&& ctx.getChannel().isConnected() && responseSent.compareAndSet(false, true))
						writeResponse(ctx, message.getMessageFactory().create(messageTransport.getOutputBuffer()),
								requestSequenceId, DispatcherContext.isResponseOrderingRequired(ctx));

				} catch (TException e) {
					onDispatchException(ctx, e);
				}
			});
		} catch (RejectedExecutionException ex) {
			sendTApplicationException(
					new TApplicationException(TApplicationException.INTERNAL_ERROR, "Server overloaded ."), ctx,
					message, requestSequenceId, messageTransport, inProtocol, outProtocol);
		}
	}

	private void sendTApplicationException(TApplicationException x, ChannelHandlerContext ctx, TNettyMessage request,
			int responseSequenceId, TNettyTransport requestTransport, TProtocol inProtocol, TProtocol outProtocol) {
		if (ctx.getChannel().isConnected()) {
			try {
				TMessage message = inProtocol.readMessageBegin();
				outProtocol.writeMessageBegin(new TMessage(message.name, TMessageType.EXCEPTION, message.seqid));
				x.write(outProtocol);
				outProtocol.writeMessageEnd();
				requestTransport.setTApplicationException(x);
				outProtocol.getTransport().flush();
				writeResponse(ctx, request.getMessageFactory().create(requestTransport.getOutputBuffer()),
						responseSequenceId, DispatcherContext.isResponseOrderingRequired(ctx));
			} catch (TException ex) {
				onDispatchException(ctx, ex);
			}
		}
	}

	private void onDispatchException(ChannelHandlerContext ctx, Throwable t) {
		Channels.fireExceptionCaught(ctx, t);
		closeChannel(ctx);
	}

	private void writeResponse(ChannelHandlerContext ctx, TNettyMessage response, int responseSequenceId,
			boolean isOrderedResponsesRequired) {
		if (isOrderedResponsesRequired)
			writeResponseInOrder(ctx, response, responseSequenceId);
		else {
			Channels.write(ctx.getChannel(), response);
			lastResponseWrittenId.incrementAndGet();
		}
	}

	private void writeResponseInOrder(ChannelHandlerContext ctx, TNettyMessage response, int responseSequenceId) {
		synchronized (responseMap) {
			int currentResponseId = lastResponseWrittenId.get() + 1;
			if (responseSequenceId != currentResponseId)
				responseMap.put(responseSequenceId, response);
			else {
				// 写入下一行的response.
				do {
					Channels.write(ctx.getChannel(), response);
					lastResponseWrittenId.incrementAndGet();
					++currentResponseId;
					response = responseMap.remove(currentResponseId);
				} while (null != response);
				
				if (DispatcherContext.isChannelReadBlocked(ctx)
						&& dispatcherSequenceId.get() <= lastResponseWrittenId.get() + queuedResponseLimit)
					DispatcherContext.unblockChannelReads(ctx);
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		// 捕获外部异常,并关闭通道
		closeChannel(ctx);
		// 写日志
		ctx.sendUpstream(e);
	}

	private void closeChannel(ChannelHandlerContext ctx) {
		if (ctx.getChannel().isOpen())
			ctx.getChannel().close();
	}

	@Override
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		DispatcherContext.unblockChannelReads(ctx);
		super.channelOpen(ctx, e);
	}

	private static class DispatcherContext {

		private ReadBlockedState readBlockedState = ReadBlockedState.NOT_BLOCKED;
		private boolean responseOrderingRequired = false;
		private boolean responseOrderingRequirementInitialized = false;

		public static boolean isChannelReadBlocked(ChannelHandlerContext ctx) {
			return getDispatcherContext(ctx).readBlockedState == ReadBlockedState.BLOCKED;
		}

		public static void blockChannelReads(ChannelHandlerContext ctx) {
			getDispatcherContext(ctx).readBlockedState = ReadBlockedState.BLOCKED;
			ctx.getChannel().setReadable(false);
		}

		public static void unblockChannelReads(ChannelHandlerContext ctx) {
			getDispatcherContext(ctx).readBlockedState = ReadBlockedState.NOT_BLOCKED;
			ctx.getChannel().setReadable(true);
		}

		public static void setResponseOrderingRequired(ChannelHandlerContext ctx, boolean required) {
			DispatcherContext dispatcherContext = getDispatcherContext(ctx);
			dispatcherContext.responseOrderingRequirementInitialized = true;
			dispatcherContext.responseOrderingRequired = required;
		}

		public static boolean isResponseOrderingRequired(ChannelHandlerContext ctx) {
			return getDispatcherContext(ctx).responseOrderingRequired;
		}

		public static boolean isResponseOrderingRequirementInitialized(ChannelHandlerContext ctx) {
			return getDispatcherContext(ctx).responseOrderingRequirementInitialized;
		}

		private static DispatcherContext getDispatcherContext(ChannelHandlerContext ctx) {
			DispatcherContext dispatcherContext;
			Object attachment = ctx.getAttachment();
			if (attachment == null) {
				// 如果没有上下文就创建一个
				dispatcherContext = new DispatcherContext();
				ctx.setAttachment(dispatcherContext);
			} else if (attachment instanceof DispatcherContext)
				dispatcherContext = (DispatcherContext) attachment;
			else
				throw new IllegalStateException(
						"NettyDispatcher handler context should be of type NettyDispatcher.DispatcherContext .");
			return dispatcherContext;
		}

		private enum ReadBlockedState {
			NOT_BLOCKED, BLOCKED,
		}

	}

}
