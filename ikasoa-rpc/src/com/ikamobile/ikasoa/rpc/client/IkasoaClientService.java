package com.ikamobile.ikasoa.rpc.client;

import com.ikamobile.ikasoa.core.STException;
import com.ikamobile.ikasoa.core.thrift.Factory;
import com.ikamobile.ikasoa.core.thrift.client.ThriftClient;
import com.ikamobile.ikasoa.rpc.BaseGetService;
import com.ikamobile.ikasoa.rpc.IkasoaException;
import com.ikamobile.ikasoa.rpc.handler.ClientInvocationContext;
import com.ikamobile.ikasoa.rpc.handler.ClientInvocationHandler;
import com.ikamobile.ikasoa.rpc.handler.ProtocolHandler;

/**
 * 客户端服务
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class IkasoaClientService<T1, T2> implements BaseGetService<T1, T2> {

	private Factory factory;

	private ThriftClient thriftClient;

	protected String serviceKey = null;

	protected ProtocolHandler<T1, T2> protocolHandler;

	// TODO:sulei
	protected ClientInvocationHandler invocationHandler;

	private ClientInvocationContext context = new ClientInvocationContext();

	public IkasoaClientService(Factory factory, ThriftClient thriftClient, ProtocolHandler<T1, T2> protocolHandler) {
		this.factory = factory;
		this.thriftClient = thriftClient;
		this.protocolHandler = protocolHandler;
	}

	public IkasoaClientService(Factory factory, ThriftClient thriftClient, String serviceKey,
			ProtocolHandler<T1, T2> protocolHandler, ClientInvocationHandler invocationHandler) {
		this.factory = factory;
		this.thriftClient = thriftClient;
		this.serviceKey = serviceKey;
		this.protocolHandler = protocolHandler;
		this.invocationHandler = invocationHandler;
	}

	@Override
	public T2 get(T1 arg) throws Throwable {
		if (invocationHandler != null) {
			context.setServerHost(thriftClient.getServerHost());
			context.setServerPort(thriftClient.getServerPort());
			context.setServiceKey(serviceKey);
			context.setArgObject(arg);
			invocationHandler.before(context);
		}
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
		context.setArgStr(argStr);
		// 执行操作,获取返回值
		String resultStr = "";
		try {
			resultStr = factory.getService(thriftClient, serviceKey).get(argStr);
		} catch (STException e) {
			throw new IkasoaException("Thrift get exception !", e);
		} finally {
			thriftClient.close();
		}
		context.setResultStr(resultStr);
		// 返回值转换
		Throwable throwable = null;
		try {
			throwable = protocolHandler.strToThrowable(resultStr);
		} catch (Throwable t) {
			throw new IkasoaException("Execute 'strToThrowable' function exception !", t);
		}
		// 判断是否为异常返回,如果是就抛出异常
		if (throwable != null) {
			if (invocationHandler != null) {
				invocationHandler.exception(context, throwable);
			}
			throw throwable;
		}
		// 不是异常返回就返回正常值
		try {
			T2 result = protocolHandler.strToResult(resultStr);
			if (invocationHandler != null) {
				context.setResultObject(result);
				invocationHandler.after(context);
			}
			return result;
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
