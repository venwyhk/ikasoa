package com.ikasoa.rpc;

import java.lang.reflect.Method;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.ikasoa.core.utils.ObjectUtil;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 服务标识对象
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@Data
@Slf4j
public class ServiceKey {

	/**
	 * 接口名称
	 */
	private String name;

	/**
	 * 接口类类型
	 */
	private Class<?> iClass;

	/**
	 * 方法名称
	 */
	private String methodName;

	/**
	 * 返回值类型
	 */
	private Class<?> returnType;

	/**
	 * 参数类型集合
	 */
	private Class<?>[] parameterTypes;

	/**
	 * 异常类型集合
	 */
	@JSONField(serialize = false)
	private Class<?>[] exceptionTypes;

	public ServiceKey(String name, Method method) {
		this.name = name;
		buildMethod(method);
	}

	public ServiceKey(Class<?> iClass, Method method) {
		this.iClass = iClass;
		buildMethod(method);
	}

	private void buildMethod(Method method) {
		if (ObjectUtil.isNotNull(method)) {
			this.methodName = method.getName();
			this.returnType = method.getReturnType();
			this.parameterTypes = method.getParameterTypes();
			this.exceptionTypes = method.getExceptionTypes();
		} else
			log.warn("Method object is null !");
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this).replaceAll(":", "|");
	}

}
