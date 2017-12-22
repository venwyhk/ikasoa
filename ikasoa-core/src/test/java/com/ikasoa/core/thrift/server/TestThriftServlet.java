package com.ikasoa.core.thrift.server;

import com.ikasoa.core.thrift.server.impl.ServletThriftServerImpl;

/**
 * 用于测试的Servlet
 */
public class TestThriftServlet extends ThriftServlet {

	private static final long serialVersionUID = 1L;

	private static ThriftServer server = new ServletThriftServerImpl("TestThriftServlet",
			new CompactThriftServerConfiguration(),
			new ThriftSimpleService.Processor<ThriftSimpleService.Iface>(new ThriftSimpleServiceImpl()));

	public TestThriftServlet() {
		super(server);
	}

}
