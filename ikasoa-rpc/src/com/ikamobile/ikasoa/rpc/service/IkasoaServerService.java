package com.ikamobile.ikasoa.rpc.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ikamobile.ikasoa.rpc.IkasoaException;
import com.ikamobile.ikasoa.rpc.handler.ProtocolHandler;

/**
 * IKASOA服务端服务实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class IkasoaServerService extends AbstractGetService<Object[], Object> {

	private static final Logger LOG = LoggerFactory.getLogger(IkasoaServerService.class);

	private Object classObj;

	private Method method;

	public IkasoaServerService(Object classObj, Method method, ProtocolHandler<Object[], Object> protocolHandler) {
		this.classObj = classObj;
		this.method = method;
		this.protocolHandler = protocolHandler;
	}

	@Override
	public Object get(Object[] args) throws IkasoaException {
		if (method == null) {
			throw new IkasoaException("'method' is null !");
		}
		if (classObj == null) {
			throw new IkasoaException("'classObj' is null !");
		}
		if (args == null) {
			LOG.debug("'args' is null , Will create default args object .");
			args = new Object[] {};
		}
		LOG.debug("Execute class '" + classObj.getClass().getName() + "' function '" + method.getName()
				+ "' , parameters is '" + args.toString() + "' .");
		method.setAccessible(true);
		try {
			return method.invoke(classObj, args);
		} catch (IllegalAccessException e) {
			throw new IkasoaException("'GET' reflect exception !", e);
		} catch (IllegalArgumentException e) {
			throw new IkasoaException("'GET' reflect exception !", e);
		} catch (InvocationTargetException e) {
			LOG.debug(e.getTargetException().toString());
			return e.getTargetException();
		}
	}

	public Object getClassObj() {
		return classObj;
	}

	public void setClassObj(Object classObj) {
		this.classObj = classObj;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

}