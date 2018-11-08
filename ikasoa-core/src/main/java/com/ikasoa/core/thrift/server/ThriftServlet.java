package com.ikasoa.core.thrift.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.thrift.server.TServlet;

/**
 * ThriftServlet
 * <p>
 * 通过Servlet来提供服务.
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.2
 */
public class ThriftServlet extends TServlet {

	private static final long serialVersionUID = 1L;

	private String serverName;

	/**
	 * 构造方法
	 * 
	 * @param server
	 *            ThriftServer对象(这里一般用<i>ServletThriftServerImpl</i>实现)
	 */
	public ThriftServlet(ThriftServer server) {
		super(server.getProcessor(), server.getServerConfiguration().getProtocolFactory());
		serverName = server.getServerName();
	}

	/**
	 * 验证服务是否启动
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter w = response.getWriter();
		w.write(String.format("This is a ikasoa server (%s) .", serverName));
		w.close();
	}

}
