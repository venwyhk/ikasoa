package com.ikasoa.core.thrift.server;

import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.server.TThreadedSelectorServer;

/**
 * Thrift服务参数处理对象
 * <p>
 * 可通过重写相应的方法,达到修改Thrift服务参数参数的目的.
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.4.5
 */
public class ServerArgsAspect {

	public TServer.Args tServerArgsAspect(TServer.Args args) {
		return args;
	}

	public TThreadPoolServer.Args tThreadPoolServerArgsAspect(TThreadPoolServer.Args args) {
		return args;
	}

	public TThreadedSelectorServer.Args tThreadedSelectorServerArgsAspect(TThreadedSelectorServer.Args args) {
		return args;
	}

	public TNonblockingServer.Args tNonblockingServerArgsAspect(TNonblockingServer.Args args) {
		return args;
	}

	public THsHaServer.Args tHsHaServerArgsAspect(THsHaServer.Args args) {
		return args;
	}

}
