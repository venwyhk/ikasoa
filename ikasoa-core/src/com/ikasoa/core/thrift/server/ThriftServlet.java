package com.ikasoa.core.thrift.server;

import org.apache.thrift.server.TServlet;

/**
 * ThriftServlet
 * <p>
 * 通过Servlet来提供服务.
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.2
 */
@SuppressWarnings("serial")
public class ThriftServlet extends TServlet {

	/**
	 * 构造方法
	 * 
	 * @param server
	 *            ThriftServer对象(这里一般用<i>ServletThriftServerImpl</i>实现)
	 */
	public ThriftServlet(ThriftServer server) {
		super(server.getProcessor(), server.getThriftServerConfiguration().getProtocolFactory());
	}

}
