package com.ikamobile.ikasoa.core.thrift.service;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TMemoryBuffer;
import org.junit.Test;
import com.ikamobile.ikasoa.core.STException;
import com.ikamobile.ikasoa.core.thrift.Factory;
import com.ikamobile.ikasoa.core.thrift.GeneralFactory;
import com.ikamobile.ikasoa.core.thrift.client.ThriftClient;
import com.ikamobile.ikasoa.core.thrift.server.ThriftServer;
import com.ikamobile.ikasoa.core.thrift.service.base.ArgsThriftBase;
import com.ikamobile.ikasoa.core.thrift.service.base.ResultThriftBase;

import junit.framework.TestCase;

/**
 * 通用服务单元测试
 */
public class ServiceTest extends TestCase {

	private String testString = "12345678abcdefg";

	@Test
	public void testDefaultServiceImpl() {
		int serverPort = 9005;
		Factory factory = new GeneralFactory();
		ThriftServer thriftServer = factory.getThriftServer(serverPort, new ServiceImpl());
		thriftServer.run();
		ThriftClient thriftClient = factory.getThriftClient("localhost", serverPort);
		try {
			Service service = factory.getService(thriftClient);
			Thread.sleep(500);
			assertEquals(service.get(testString), testString);
		} catch (Exception e) {
			fail();
		} finally {
			thriftServer.stop();
		}
	}

	@Test
	public void testThriftBaseReadWrite() {
		TBinaryProtocol protocol = new TBinaryProtocol(new TMemoryBuffer(16));
		ArgsThriftBase args = new ArgsThriftBase(testString);
		ResultThriftBase result = new ResultThriftBase();
		try {
			args.write(protocol);
			result.read(protocol);
			assertEquals(args.getStr(), result.getStr(), testString);
		} catch (TException e) {
			fail();
		}
	}

	private class ServiceImpl implements Service {
		@Override
		public String get(String arg) throws STException {
			return arg;
		}
	}

}
