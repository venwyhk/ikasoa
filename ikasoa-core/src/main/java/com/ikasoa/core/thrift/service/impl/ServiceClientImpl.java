package com.ikasoa.core.thrift.service.impl;

import org.apache.thrift.TApplicationException;
import org.apache.thrift.TException;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;
import com.ikasoa.core.IkasoaException;
import com.ikasoa.core.thrift.service.Processor;
import com.ikasoa.core.thrift.service.Service;
import com.ikasoa.core.thrift.service.base.ArgsThriftBase;
import com.ikasoa.core.thrift.service.base.ResultThriftBase;

import lombok.extern.slf4j.Slf4j;

/**
 * 通用服务客户端实现类
 * <p>
 * 服务提供者需要自建一个服务端的实现类.
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.2
 */
@Slf4j
public class ServiceClientImpl extends TServiceClient implements Service {

	public ServiceClientImpl(TProtocol prot) {
		super(prot);
	}

	@Override
	public String get(String arg) throws IkasoaException {
		TTransport transport = oprot_.getTransport();
		try {
			if (!transport.isOpen())
				transport.open();
			log.debug("Transport is open .");
			sendGet(arg);
			return recvGet();
		} catch (TException e) {
			transport.close();
			log.debug("Transport is close .");
			throw new IkasoaException("Execute failed !", e);
		}
	}

	private void sendGet(String arg) throws TException {
		sendBase(Processor.FUNCTION_NAME, new ArgsThriftBase(arg));
	}

	String recvGet() throws TException {
		ResultThriftBase result = new ResultThriftBase();
		receiveBase(result, Processor.FUNCTION_NAME);
		if (result.isSet(null))
			return result.getStr();
		else
			throw new TApplicationException(TApplicationException.MISSING_RESULT, "Get failed: unknown result");
	}

}
