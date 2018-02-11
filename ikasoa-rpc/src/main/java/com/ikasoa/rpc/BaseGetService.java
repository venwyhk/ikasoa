package com.ikasoa.rpc;

/**
 * 基础服务接口
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@FunctionalInterface
public interface BaseGetService<T, R> {

	/**
	 * 获取默认的服务对象
	 * 
	 * @param arg
	 *            参数对象
	 * @return R 返回对象
	 * @exception Throwable
	 *                抛出
	 */
	R get(T arg) throws Throwable;

}
