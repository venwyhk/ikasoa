package com.ikasoa.rpc.handler;

/**
 * 客户端拦截器上下文对象
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class ClientInvocationContext {

	// 服务器地址
	private String serverHost;

	// 服务器端口
	private int serverPort;

	// 服务标识
	private String serviceKey;

	// 参数对象
	private Object argObject;

	// 参数序列化后的字符串
	private String argStr;

	// 返回值对象
	private Object resultObject;

	// 返回值列化后前的字符串
	private String resultStr;

	public ClientInvocationContext() {
	}

	public String getServerHost() {
		return serverHost;
	}

	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public String getServiceKey() {
		return serviceKey;
	}

	public void setServiceKey(String serviceKey) {
		this.serviceKey = serviceKey;
	}

	public Object getArgObject() {
		return argObject;
	}

	public void setArgObject(Object argObject) {
		this.argObject = argObject;
	}

	public String getArgStr() {
		return argStr;
	}

	public void setArgStr(String argStr) {
		this.argStr = argStr;
	}

	public Object getResultObject() {
		return resultObject;
	}

	public void setResultObject(Object resultObject) {
		this.resultObject = resultObject;
	}

	public String getResultStr() {
		return resultStr;
	}

	public void setResultStr(String resultStr) {
		this.resultStr = resultStr;
	}

}
