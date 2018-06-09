package com.ikasoa.rpc.handler;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * 客户端拦截器上下文对象
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@Data
@RequiredArgsConstructor
public class ClientInvocationContext {

	@NonNull
	private String uuid;

	/**
	 * 服务器地址
	 */
	private String serverHost;

	/**
	 * 服务器端口
	 */
	private int serverPort;

	/**
	 * 服务标识
	 */
	private String serviceKey;

	/**
	 * 参数对象
	 */
	private Object argObject;

	/**
	 * 参数序列化后的字符串
	 */
	private String argStr;

	/**
	 * 返回值对象
	 */
	private Object resultObject;

	/**
	 * 返回值列化后前的字符串
	 */
	private String resultStr;

}
