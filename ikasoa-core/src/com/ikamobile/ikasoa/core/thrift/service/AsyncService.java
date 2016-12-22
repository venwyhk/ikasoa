package com.ikamobile.ikasoa.core.thrift.service;

import org.apache.thrift.async.AsyncMethodCallback;

import com.ikamobile.ikasoa.core.STException;
import com.ikamobile.ikasoa.core.thrift.service.impl.CallBack;

/**
 * 异步服务接口
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.4.2
 */
public interface AsyncService {

	/**
	 * 通用异步方法
	 * <p>
	 * 使用异步服务时,需在客户端调用该方法.传入参数和回调方法对象后,可在回调方法内获取返回结果或异常.
	 * 
	 * @param arg
	 *            参数
	 * @param resultHandler
	 *            异步回调方法
	 * @exception STException
	 *                异常
	 */
	public void get(String arg, AsyncMethodCallback<CallBack> resultHandler) throws STException;

}