package com.ikasoa.core.thrift.server;

import org.apache.thrift.TException;

/**
 * 测试Thrift服务实现
 */
public class ThriftSimpleServiceImpl implements ThriftSimpleService.Iface {
	@Override
	public String get(String arg) throws TException {
		return arg;
	}
}
