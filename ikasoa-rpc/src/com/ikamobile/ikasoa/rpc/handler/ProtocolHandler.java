package com.ikamobile.ikasoa.rpc.handler;

/**
 * 转换协议处理器接口
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public interface ProtocolHandler<T1, T2> {

	/**
	 * 字符串转为参数对象
	 * 
	 * @param str
	 *            参数字符串
	 * @return T1 参数对象
	 */
	public T1 strToArg(String str);

	/**
	 * 参数对象转为字符串
	 * 
	 * @param arg
	 *            参数对象
	 * @return String
	 */
	public String argToStr(T1 arg);

	/**
	 * 返回值对象转为字符串
	 * 
	 * @param result
	 *            返回值对象
	 * @return String
	 */
	public String resultToStr(T2 result);

	/**
	 * 字符串转为返回值对象
	 * 
	 * @param str
	 *            返回值字符串
	 * @return T2 返回值对象
	 */
	public T2 strToResult(String str);

	/**
	 * 字符串转为异常对象(如果没有异常,则返回null)
	 * 
	 * @param str
	 *            返回值字符串
	 * @return Throwable 异常对象
	 */
	public Throwable strToThrowable(String str);

	/**
	 * 获取返回数据对象
	 * 
	 * @return ReturnData 返回数据对象
	 */
	public ReturnData getReturnData();

}
