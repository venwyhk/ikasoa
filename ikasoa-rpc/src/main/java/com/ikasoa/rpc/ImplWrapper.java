package com.ikasoa.rpc;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Data;
import lombok.NonNull;

/**
 * 接口实现对象包装器
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.3.2
 */
@AllArgsConstructor
@Data
@RequiredArgsConstructor
public class ImplWrapper {

	/**
	 * 接口实现类类型
	 */
	@NonNull
	private Class<?> implClass;

	/**
	 * 接口实现类
	 */
	private Object implObject;

}
