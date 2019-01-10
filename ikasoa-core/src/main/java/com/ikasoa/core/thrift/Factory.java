package com.ikasoa.core.thrift;

import com.ikasoa.core.thrift.client.ThriftClientFactory;
import com.ikasoa.core.thrift.server.ThriftServerFactory;
import com.ikasoa.core.thrift.service.ThriftServiceFactory;

/**
 * 通用工厂接口
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.2
 */
public interface Factory extends ThriftServerFactory, ThriftClientFactory, ThriftServiceFactory {
}
