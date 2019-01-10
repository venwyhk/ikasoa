package com.ikasoa.core.thrift.server;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.thrift.TProcessor;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;

import com.ikasoa.core.Server;

/**
 * Thrift服务器接口
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public interface ThriftServer extends Server {

	/**
	 * Thrift服务传输类型
	 * <p>
	 * 通常为<i>org.apache.thrift.transport.TServerSocket</i>
	 * ,但如果采用NonblockingServer来实现Thrift服务,这里就需要用 <i>TNonblockingServerSocket</i>. .
	 * 
	 * @return TServerTransport 服务传输类型
	 * @exception TTransportException
	 *                TransportExceptions
	 */
	TServerTransport getTransport() throws TTransportException;

	/**
	 * 获取Thrift服务配置
	 * 
	 * @return ServerConfiguration 服务配置
	 */
	ServerConfiguration getServerConfiguration();

	/**
	 * 获取Thrift处理器
	 * 
	 * @return TProcessor Thrift处理器
	 */
	TProcessor getProcessor();

	/**
	 * 停止线程池
	 * 
	 * @param executorService
	 *            线程池
	 */
	default void shutdownExecutor(ExecutorService executorService) {
		if (executorService != null && !executorService.isShutdown()) {
			executorService.shutdown();
			try {
				while (executorService != null && !executorService.isTerminated())
					executorService.awaitTermination(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				executorService.shutdownNow();
				Thread.currentThread().interrupt();
			}
		}
		executorService = null;
	}

	default void beforeStart(ServerAspect serverAspect) {
		Optional.ofNullable(serverAspect).ifPresent(sAspect -> sAspect.beforeStart(getServerName(), getServerPort(),
				getServerConfiguration(), getProcessor(), this));
	}

	default void afterStart(ServerAspect serverAspect) {
		Optional.ofNullable(serverAspect).ifPresent(sAspect -> sAspect.afterStart(getServerName(), getServerPort(),
				getServerConfiguration(), getProcessor(), this));
	}

	default void beforeStop(ServerAspect serverAspect) {
		Optional.ofNullable(serverAspect).ifPresent(sAspect -> sAspect.beforeStop(getServerName(), getServerPort(),
				getServerConfiguration(), getProcessor(), this));
	}

	default void afterStop(ServerAspect serverAspect) {
		Optional.ofNullable(serverAspect).ifPresent(sAspect -> sAspect.afterStop(getServerName(), getServerPort(),
				getServerConfiguration(), getProcessor(), this));
	}

}