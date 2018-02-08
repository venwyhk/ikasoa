package com.ikasoa.core.thrift.service;

import org.apache.thrift.TProcessor;

/**
 * 服务处理器接口
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.4.2
 */
public interface Processor extends TProcessor {

	String FUNCTION_NAME = "get";

}
