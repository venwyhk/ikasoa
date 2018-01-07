package com.ikasoa.core.thrift.service.impl;

import java.io.IOException;

import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.async.TAsyncClient;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TNonblockingTransport;

import com.ikasoa.core.STException;
import com.ikasoa.core.thrift.service.AsyncService;

/**
 * 异步服务客户端实现类
 * <p>
 * 服务提供者需要自建一个服务端的实现类.
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.4.3
 */
public class AsyncServiceClientImpl extends TAsyncClient implements AsyncService {

	public AsyncServiceClientImpl(TProtocolFactory protocolFactory, TAsyncClientManager manager,
			TNonblockingTransport transport) {
		super(protocolFactory, manager, transport);
	}

	public AsyncServiceClientImpl(TProtocolFactory protocolFactory, TNonblockingTransport transport)
			throws IOException {
		super(protocolFactory, new TAsyncClientManager(), transport);
	}

	@Override
	public void get(String arg, AsyncMethodCallback<String> resultHandler) throws STException {
		checkReady();
		CallBack methodCall;
		try {
			methodCall = new CallBack(arg, resultHandler, this, ___protocolFactory, ___transport);
			super.___currentMethod = methodCall;
			super.___manager.call(methodCall);
		} catch (TException e) {
			throw new STException(e);
		}
	}

}
