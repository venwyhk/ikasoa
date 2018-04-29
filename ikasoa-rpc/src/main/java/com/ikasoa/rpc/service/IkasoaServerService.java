package com.ikasoa.rpc.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ikasoa.rpc.RpcException;
import com.ikasoa.rpc.handler.ProtocolHandler;

import lombok.Getter;
import lombok.Setter;

/**
 * IKASOA服务端服务实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class IkasoaServerService extends AbstractGetService<Object[], Object> {

	private static final Logger LOG = LoggerFactory.getLogger(IkasoaServerService.class);

	@Getter
	@Setter
	private Object classObj;

	@Getter
	@Setter
	private Method method;

	public IkasoaServerService(Object classObj, Method method, ProtocolHandler<Object[], Object> protocolHandler) {
		this.classObj = classObj;
		this.method = method;
		this.protocolHandler = protocolHandler;
	}

	@Override
	public Object get(Object[] args) throws RpcException {
		if (method == null)
			throw new RpcException("'method' is null !");
		if (classObj == null)
			throw new RpcException("'classObj' is null !");
		if (args == null) {
			LOG.debug("'args' is null , Will create default args object .");
			args = new Object[] {};
		}
		LOG.debug("Execute class '{}' function '{}' , parameters is '{}' .", classObj.getClass().getName(),
				method.getName(), args.toString());
		method.setAccessible(Boolean.TRUE);
		try {
			return method.invoke(classObj, args);
		} catch (IllegalAccessException e) {
			throw new RpcException("'GET' reflect exception !", e);
		} catch (IllegalArgumentException e) {
			throw new RpcException("'GET' reflect exception !", e);
		} catch (InvocationTargetException e) {
			LOG.debug(e.getTargetException().toString());
			return e.getTargetException();
		}
	}

}