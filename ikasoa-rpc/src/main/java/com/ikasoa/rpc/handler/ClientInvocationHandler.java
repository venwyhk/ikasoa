package com.ikasoa.rpc.handler;

/**
 * 客户端调用拦截器接口
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public interface ClientInvocationHandler {

	/**
	 * 开始
	 * 
	 * @param context
	 *            上下文
	 * @return ClientInvocationContext
	 */
	ClientInvocationContext before(ClientInvocationContext context);

	/**
	 * 执行前
	 * 
	 * @param context
	 *            上下文
	 * @return ClientInvocationContext
	 */
	ClientInvocationContext invoke(ClientInvocationContext context);

	/**
	 * 正常结束
	 * 
	 * @param context
	 *            上下文
	 */
	void after(ClientInvocationContext context);

	/**
	 * 异常结束
	 * 
	 * @param context
	 *            上下文
	 * @param throwable
	 *            异常对象
	 */
	void exception(ClientInvocationContext context, Throwable throwable);

}
