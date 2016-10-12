package com.ikamobile.ikasoa.core.thrift.server;

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

	public TServer.Args TServerArgsAspect(TServer.Args args) {
		return args;
	}

	public TThreadPoolServer.Args TThreadPoolServerArgsAspect(TThreadPoolServer.Args args) {
		return args;
	}

	public TThreadedSelectorServer.Args TThreadedSelectorServerArgsAspect(TThreadedSelectorServer.Args args) {
		return args;
	}

	public TNonblockingServer.Args TNonblockingServerArgsAspect(TNonblockingServer.Args args) {
		return args;
	}

	public THsHaServer.Args THsHaServerArgsAspect(THsHaServer.Args args) {
		return args;
	}

}
