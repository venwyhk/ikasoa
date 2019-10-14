package com.ikasoa.rpc.client;

import java.util.UUID;

import com.ikasoa.core.thrift.Factory;
import com.ikasoa.core.thrift.client.ThriftClient;
import com.ikasoa.core.utils.ObjectUtil;
import com.ikasoa.rpc.BaseGetService;
import com.ikasoa.rpc.RpcException;
import com.ikasoa.rpc.handler.ClientInvocationContext;
import com.ikasoa.rpc.handler.ClientInvocationHandler;
import com.ikasoa.rpc.handler.ProtocolHandler;

import lombok.Getter;
import lombok.Setter;

/**
 * 客户端服务
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class IkasoaClientService<T, R> implements BaseGetService<T, R> {

	private Factory factory;

	@Getter
	@Setter
	private ThriftClient thriftClient;

	protected String serviceKey;

	protected ProtocolHandler<T, R> protocolHandler;

	// TODO: Larry
	protected ClientInvocationHandler invocationHandler;

	public IkasoaClientService(Factory factory, ThriftClient thriftClient, ProtocolHandler<T, R> protocolHandler) {
		this.factory = factory;
		this.thriftClient = thriftClient;
		this.protocolHandler = protocolHandler;
	}

	public IkasoaClientService(Factory factory, ThriftClient thriftClient, String serviceKey,
			ProtocolHandler<T, R> protocolHandler, ClientInvocationHandler invocationHandler) {
		this.factory = factory;
		this.thriftClient = thriftClient;
		this.serviceKey = serviceKey;
		this.protocolHandler = protocolHandler;
		this.invocationHandler = invocationHandler;
	}

	@Override
	public R get(T arg) throws Throwable {
		ClientInvocationContext context = null;
		if (ObjectUtil.isNotNull(invocationHandler)) {
			context = new ClientInvocationContext(UUID.randomUUID().toString());
			context.setServerHost(thriftClient.getServerHost());
			context.setServerPort(thriftClient.getServerPort());
			context.setServiceKey(serviceKey);
			context.setArgObject(arg);
			context = invocationHandler.before(context);
		}
		// 参数转换
		if (ObjectUtil.isNull(protocolHandler))
			throw new RpcException("'protocolHandler' is null !");
		String argStr = null;
		try {
			argStr = protocolHandler.argToStr(arg);
		} catch (Exception e) {
			throw new RpcException("Execute 'argToStr' function exception !", e);
		}
		if (!ObjectUtil.orIsNull(context, invocationHandler)) {
			context.setArgStr(argStr);
			argStr = invocationHandler.invoke(context).getArgStr();
		}
		// 执行操作,获取返回值
		String resultStr = "";
		try {
			resultStr = factory.getService(thriftClient, serviceKey).get(argStr);
		} catch (Exception e) {
			throw new RpcException("Thrift get exception !", e);
		} finally {
			thriftClient.close();
		}
		if (!ObjectUtil.orIsNull(context, invocationHandler))
			context.setResultStr(resultStr);
		// 返回值转换
		Throwable throwable = null;
		try {
			throwable = protocolHandler.strToThrowable(resultStr);
		} catch (Exception e) {
			throw new RpcException("Execute 'strToThrowable' function exception !", e);
		}
		// 判断是否为异常返回,如果是就抛出异常
		if (ObjectUtil.isNotNull(throwable)) {
			if (ObjectUtil.isNotNull(invocationHandler)) {
				invocationHandler.exception(context, throwable);
				context = null;
			}
			throw throwable;
		}
		// 不是异常返回就返回正常值
		try {
			R result = protocolHandler.strToResult(resultStr);
			if (!ObjectUtil.orIsNull(context, invocationHandler)) {
				context.setResultObject(result);
				invocationHandler.after(context);
				context = null;
			}
			return result;
		} catch (Exception e) {
			throw new RpcException("Execute 'strToResult' function exception !", e);
		}
	}

}
