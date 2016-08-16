package com.ikamobile.ikasoa.rpc;

/**
 * 基础服务接口
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public interface BaseGetService<T1, T2> {

	/**
	 * 获取默认的ThriftServer对象
	 * 
	 * @param arg
	 *            参数对象
	 * @return T2 返回对象
	 * @exception Throwable
	 *                抛出
	 */
	public T2 get(T1 arg) throws Throwable;

}
