package com.ikamobile.ikasoa.rpc;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * 服务标识对象
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class ServiceKey {

	private static final Logger LOG = LoggerFactory.getLogger(ServiceKey.class);

	// 接口类类型
	private Class<?> iClass;

	// 方法名称
	private String methodName;

	// 返回值类型
	private Class<?> returnType;

	// 参数类型集合
	private Class<?>[] parameterTypes;

	// 异常类型集合
	@JSONField(serialize = false)
	private Class<?>[] exceptionTypes;

	public ServiceKey(Class<?> iClass, Method method) {
		this.iClass = iClass;
		if (method != null) {
			this.methodName = method.getName();
			this.returnType = method.getReturnType();
			this.parameterTypes = method.getParameterTypes();
			this.exceptionTypes = method.getExceptionTypes();
		} else {
			LOG.warn("Method object is null !");
		}
	}

	public Class<?> getIClass() {
		return iClass;
	}

	public void setIClass(Class<?> iClass) {
		this.iClass = iClass;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Class<?> getReturnType() {
		return returnType;
	}

	public void setReturnType(Class<?> returnType) {
		this.returnType = returnType;
	}

	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}

	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	public Class<?>[] getExceptionTypes() {
		return exceptionTypes;
	}

	public void setExceptionTypes(Class<?>[] exceptionTypes) {
		this.exceptionTypes = exceptionTypes;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this).replaceAll(":", "|");
	}

}
