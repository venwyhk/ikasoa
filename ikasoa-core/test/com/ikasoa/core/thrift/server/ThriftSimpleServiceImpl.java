package com.ikasoa.core.thrift.server;

import org.apache.thrift.TException;

public class ThriftSimpleServiceImpl implements ThriftSimpleService.Iface {
	@Override
	public String get(String arg) throws TException {
		return arg;
	}
}
