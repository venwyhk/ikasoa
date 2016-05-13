package com.ikamobile.ikasoa.core.thrift.service;

import com.ikamobile.ikasoa.core.STException;

/**
 * 通用服务接口
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.2
 */
public interface Service {

	/**
	 * 通用方法
	 * <p>
	 * <i>get</i>方法的参数与返回值都为<i>String</i>,在传入参数前需要将参数对象转换为<i>String</i>,可以序列化为
	 * <i>JSON</i>或<i>XML</i>等格式进行传递,在获取返回值后再转换为具体的对象.
	 * 
	 * @param arg
	 *            参数
	 * @return String 返回值
	 * @exception STException
	 */
	public String get(String arg) throws STException;

}
