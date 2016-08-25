package com.ikamobile.ikasoa.core.thrift.service.impl;

import org.apache.thrift.TApplicationException;
import org.apache.thrift.TException;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ikamobile.ikasoa.core.STException;
import com.ikamobile.ikasoa.core.thrift.service.Service;
import com.ikamobile.ikasoa.core.thrift.service.base.ArgsThriftBase;
import com.ikamobile.ikasoa.core.thrift.service.base.ResultThriftBase;

/**
 * 通用服务客户端实现类
 * <p>
 * 服务提供者需要自建一个服务端的实现类.
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.2
 */
public class ServiceClientImpl extends TServiceClient implements Service {

	private static final Logger LOG = LoggerFactory.getLogger(ServiceClientImpl.class);

	public ServiceClientImpl(TProtocol prot) {
		super(prot);
	}

	@Override
	public String get(String arg) throws STException {
		TTransport transport = oprot_.getTransport();
		try {
			if (!transport.isOpen()) {
				transport.open();
			}
			sendGet(arg);
			return recvGet();
		} catch (TException e) {
			transport.close();
			LOG.error(e.getMessage());
			throw new STException("Execute 'get' function failed !", e);
		}
	}

	private void sendGet(String arg) throws TException {
		sendBase("get", new ArgsThriftBase(arg));
	}

	String recvGet() throws TException {
		ResultThriftBase result = new ResultThriftBase();
		receiveBase(result, "get");
		if (result.isSet(null)) {
			return result.getStr();
		} else {
			throw new TApplicationException(TApplicationException.MISSING_RESULT, "get failed: unknown result");
		}
	}

}
