package com.ikamobile.ikasoa.client;

import org.sulei.core.STException;
import org.sulei.core.thrift.Factory;
import org.sulei.core.thrift.client.ThriftClient;

import com.ikamobile.ikasoa.BaseGetService;
import com.ikamobile.ikasoa.IkasoaException;
import com.ikamobile.ikasoa.handler.ProtocolHandler;

/**
 * 客户端服务
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class IkasoaClientService<T1, T2> implements BaseGetService<T1, T2> {

	private Factory factory;

	private ThriftClient thriftClient;

	protected String serviceName = null;

	protected ProtocolHandler<T1, T2> protocolHandler;

	public IkasoaClientService(Factory factory, ThriftClient thriftClient, ProtocolHandler<T1, T2> protocolHandler) {
		this.factory = factory;
		this.thriftClient = thriftClient;
		this.protocolHandler = protocolHandler;
	}

	public IkasoaClientService(Factory factory, ThriftClient thriftClient, String serviceName,
			ProtocolHandler<T1, T2> protocolHandler) {
		this.factory = factory;
		this.thriftClient = thriftClient;
		this.serviceName = serviceName;
		this.protocolHandler = protocolHandler;
	}

	@Override
	public T2 get(T1 arg) throws Throwable {
		// 参数转换
		if (protocolHandler == null) {
			throw new IkasoaException("'protocolHandler' is null !");
		}
		String argStr = null;
		try {
			argStr = protocolHandler.argToStr(arg);
		} catch (Throwable t) {
			throw new IkasoaException("Execute 'argToStr' function exception !", t);
		}
		// 执行操作,获取返回值
		String resultStr = "";
		try {
			resultStr = factory.getService(thriftClient, serviceName).get(argStr);
		} catch (STException e) {
			throw new IkasoaException("Thrift get exception !", e);
		}
		// 返回值转换
		Throwable throwable = null;
		try {
			throwable = protocolHandler.strToThrowable(resultStr);
		} catch (Throwable t) {
			throw new IkasoaException("Execute 'strToThrowable' function exception !", t);
		}
		// 判断是否为异常返回,如果是就抛出异常
		if (throwable != null) {
			throw throwable;
		}
		// 不是异常返回就返回正常值
		try {
			return protocolHandler.strToResult(resultStr);
		} catch (Throwable t) {
			throw new IkasoaException("Execute 'strToResult' function exception !", t);
		}
	}

	public ThriftClient getThriftClient() {
		return thriftClient;
	}

	public void setThriftClient(ThriftClient thriftClient) {
		this.thriftClient = thriftClient;
	}

}
