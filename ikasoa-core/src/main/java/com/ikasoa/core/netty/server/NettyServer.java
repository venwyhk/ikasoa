package com.ikasoa.core.netty.server;

import org.jboss.netty.util.ExternalResourceReleasable;

import com.ikasoa.core.thrift.server.ThriftServer;

/**
 * Netty服务器接口
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.6
 */
public interface NettyServer extends ThriftServer, ExternalResourceReleasable {
}
