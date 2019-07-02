package com.ikasoa.core.thrift.service;

import java.util.Map;

import org.apache.thrift.AsyncProcessFunction;
import org.apache.thrift.TApplicationException;
import org.apache.thrift.TBase;
import org.apache.thrift.TBaseAsyncProcessor;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.protocol.TMessageType;
import org.apache.thrift.server.AbstractNonblockingServer.AsyncFrameBuffer;

import com.ikasoa.core.IkasoaException;
import com.ikasoa.core.thrift.service.base.ArgsThriftBase;
import com.ikasoa.core.thrift.service.base.ResultThriftBase;
import com.ikasoa.core.utils.MapUtil;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 异步服务处理器
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.4.2
 */
public class AsyncServiceProcessor extends TBaseAsyncProcessor<AsyncService> implements Processor {

	public AsyncServiceProcessor(AsyncService service) {
		super(service, getProcessMap(MapUtil.newHashMap()));
	}

	@SuppressWarnings("rawtypes")
	private static Map<String, AsyncProcessFunction<AsyncService, ? extends TBase, ?>> getProcessMap(
			Map<String, AsyncProcessFunction<AsyncService, ? extends TBase, ?>> processMap) {
		processMap.put(FUNCTION_NAME, new GetAsyncProcessFunction());
		return processMap;
	}

	@Slf4j
	public static class GetAsyncProcessFunction extends AsyncProcessFunction<AsyncService, ArgsThriftBase, String> {

		public GetAsyncProcessFunction() {
			super(FUNCTION_NAME);
		}

		public ArgsThriftBase getEmptyArgsInstance() {
			return new ArgsThriftBase();
		}

		public AsyncMethodCallback<String> getResultHandler(final AsyncFrameBuffer fb, final int seqid) {
			final AsyncProcessFunction<AsyncService, ArgsThriftBase, String> fcall = this;
			return new AsyncMethodCallback<String>() {
				public void onComplete(String o) {
					ResultThriftBase result = new ResultThriftBase();
					result.setFieldValue(null, o);
					try {
						fcall.sendResponse(fb, result, TMessageType.REPLY, seqid);
						return;
					} catch (Exception e) {
						log.error("Exception writing to internal frame buffer : {}", e.getMessage());
						fb.close();
					}
				}

				public void onError(Exception e) {
					byte msgType = TMessageType.REPLY;
					TBase<?, ?> msg;
					{
						msgType = TMessageType.EXCEPTION;
						msg = (TBase<?, ?>) new TApplicationException(TApplicationException.INTERNAL_ERROR,
								e.getMessage());
					}
					try {
						fcall.sendResponse(fb, msg, msgType, seqid);
						return;
					} catch (Exception ex) {
						log.error("Exception writing to internal frame buffer : {}", ex.getMessage());
						fb.close();
					}
				}
			};
		}

		protected boolean isOneway() {
			return false;
		}

		@SneakyThrows(IkasoaException.class)
		public void start(AsyncService service, ArgsThriftBase args, AsyncMethodCallback<String> resultHandler)
				throws TException {
			log.debug("Args is : {}", args.getStr());
			service.get((String) args.getStr(), resultHandler);
		}
	}

}
