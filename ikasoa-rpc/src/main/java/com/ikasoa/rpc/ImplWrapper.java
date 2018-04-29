package com.ikasoa.rpc;

import lombok.Data;

/**
 * 接口实现对象包装器
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.3.2
 */
@Data
public class ImplWrapper {

	/**
	 * 接口实现类类型
	 */
	private Class<?> implClass;

	/**
	 * 接口实现类
	 */
	private Object implObject;

	public ImplWrapper(Class<?> implClass) {
		this.implClass = implClass;
	}

	public ImplWrapper(Class<?> implClass, Object implObject) {
		this.implClass = implClass;
		this.implObject = implObject;
	}
	
}
