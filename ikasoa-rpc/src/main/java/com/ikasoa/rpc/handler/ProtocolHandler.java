package com.ikasoa.rpc.handler;

/**
 * 转换协议处理器接口
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public interface ProtocolHandler<T, R> {

	/**
	 * 类型分隔符, 异常标识符, 空标识符
	 */
	char CT = (char) 0x96, E = (char) 0x97, V = (char) 0x98;

	/**
	 * 字符串转为参数对象
	 * 
	 * @param str
	 *            参数字符串
	 * @return T 参数对象
	 */
	T strToArg(String str);

	/**
	 * 参数对象转为字符串
	 * 
	 * @param arg
	 *            参数对象
	 * @return String
	 */
	String argToStr(T arg);

	/**
	 * 返回值对象转为字符串
	 * 
	 * @param result
	 *            返回值对象
	 * @return String
	 */
	String resultToStr(R result);

	/**
	 * 字符串转为返回值对象
	 * 
	 * @param str
	 *            返回值字符串
	 * @return R 返回值对象
	 */
	R strToResult(String str);

	/**
	 * 字符串转为异常对象(如果没有异常,则返回null)
	 * 
	 * @param str
	 *            返回值字符串
	 * @return Throwable 异常对象
	 */
	Throwable strToThrowable(String str);

	/**
	 * 获取返回数据对象
	 * 
	 * @return ReturnData 返回数据对象
	 */
	ReturnData getReturnData();

}
