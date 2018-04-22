package com.ikasoa.rpc;

import lombok.Data;

/**
 * 应用接口实现集合对象
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@Data
public class ImplClsCon {

	/**
	 * 接口实现类类型
	 */
	private Class<?> implClass;

	/**
	 * 接口实现类
	 */
	private Object implObject;

	public ImplClsCon(Class<?> implClass) {
		this.implClass = implClass;
	}

	public ImplClsCon(Class<?> implClass, Object implObject) {
		this.implClass = implClass;
		this.implObject = implObject;
	}

}
