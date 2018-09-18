package com.ikasoa.core.thrift.service;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TMemoryBuffer;
import org.junit.Test;

import com.ikasoa.core.IkasoaException;
import com.ikasoa.core.TestBase;
import com.ikasoa.core.thrift.Factory;
import com.ikasoa.core.thrift.GeneralFactory;
import com.ikasoa.core.thrift.client.ThriftClient;
import com.ikasoa.core.thrift.server.ServerArgsAspect;
import com.ikasoa.core.thrift.server.ThriftServer;
import com.ikasoa.core.thrift.server.ThriftServerConfiguration;
import com.ikasoa.core.thrift.service.base.ArgsThriftBase;
import com.ikasoa.core.thrift.service.base.ResultThriftBase;

/**
 * 通用服务单元测试
 */
public class ServiceTest extends TestBase {

	@Test
	public void testDefaultServiceImpl() {
		int serverPort = getNewPort();
		ThriftServerConfiguration thriftServerConfiguration = new ThriftServerConfiguration();
		thriftServerConfiguration.setServerArgsAspect(new ServerArgsAspect() {
			@Override
			public TThreadPoolServer.Args tThreadPoolServerArgsAspect(TThreadPoolServer.Args args) {
				args.stopTimeoutVal = 1;
				return args;
			}
		});
		Factory factory = new GeneralFactory(thriftServerConfiguration);
		ThriftServer thriftServer = factory.getThriftServer(serverPort, new TestService());
		thriftServer.run();
		ThriftClient thriftClient = factory.getThriftClient(LOCAL_HOST, serverPort);
		try {
			Thread.sleep(500);
			assertEquals(factory.getService(thriftClient).get(TEST_STRING), TEST_STRING);
		} catch (Exception e) {
			fail();
		} finally {
			thriftServer.stop();
		}
	}

	@Test
	public void testThriftBaseReadWrite() {
		TBinaryProtocol protocol = new TBinaryProtocol(new TMemoryBuffer(16));
		ArgsThriftBase args = new ArgsThriftBase(TEST_STRING);
		ResultThriftBase result = new ResultThriftBase();
		try {
			args.write(protocol);
			result.read(protocol);
			assertEquals(args.getStr(), result.getStr(), TEST_STRING);
		} catch (TException e) {
			fail();
		}
	}

	private class TestService implements Service {

		@Override
		public String get(String arg) throws IkasoaException {
			return arg;
		}

	}

}
