package com.ikasoa.rpc.service;

import java.util.List;
import java.util.Map;

import org.apache.thrift.server.TThreadPoolServer;
import org.junit.Before;
import org.junit.Test;
import com.ikasoa.core.thrift.server.ServerArgsAspect;
import com.ikasoa.core.thrift.server.ThriftServerConfiguration;
import com.ikasoa.core.utils.ListUtil;
import com.ikasoa.core.utils.MapUtil;
import com.ikasoa.core.utils.ServerUtil;
import com.ikasoa.rpc.IkasoaServer;
import com.ikasoa.rpc.ImplWrapper;
import com.ikasoa.rpc.NettyIkasoaFactory;
import com.ikasoa.rpc.ServerInfoWrapper;
import com.ikasoa.rpc.handler.impl.KryoProtocolHandlerImpl;
import com.ikasoa.rpc.handler.impl.SerializableProtocolHandlerImpl;
import com.ikasoa.rpc.handler.impl.XmlProtocolHandlerImpl;
import com.ikasoa.rpc.Configurator;
import com.ikasoa.rpc.DefaultIkasoaFactory;
import com.ikasoa.rpc.IkasoaFactory;

import junit.framework.TestCase;

/**
 * 服务调用测试
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class TestExampleService extends TestCase {

	private ThriftServerConfiguration thriftServerConfiguration = new ThriftServerConfiguration();

	@Before
	public void setUp() {
		// configurator.setClientInvocationHandler(new
		// LoggerClientInvocationHandlerImpl());
		thriftServerConfiguration.setServerArgsAspect(new ServerArgsAspect() {
			@Override
			public TThreadPoolServer.Args tThreadPoolServerArgsAspect(TThreadPoolServer.Args args) {
				args.stopTimeoutVal = 1;
				return args;
			}
		});
	}

	@Test
	public void testDefaultService() {
		Configurator configurator = new Configurator();
		configurator.setThriftServerConfiguration(thriftServerConfiguration);
		invoke(new DefaultIkasoaFactory(configurator), ServerUtil.getNewPort());
		// 测试NIO方式
		configurator.setNonBlockingIO(true);
		invoke(new DefaultIkasoaFactory(configurator), ServerUtil.getNewPort());
	}

	@Test
	public void testNettyService() {
		invoke(new NettyIkasoaFactory(), ServerUtil.getNewPort());
	}

	@Test
	public void testDefaultKryoService() throws ClassNotFoundException {
		Configurator configurator = new Configurator();
		configurator.setThriftServerConfiguration(thriftServerConfiguration);
		configurator.setProtocolHandler(new KryoProtocolHandlerImpl<>());
		invoke(new DefaultIkasoaFactory(configurator), ServerUtil.getNewPort());
		// 测试NIO方式
		configurator.setNonBlockingIO(true);
		invoke(new DefaultIkasoaFactory(configurator), ServerUtil.getNewPort());
	}

	@Test
	public void testNettyKryoService() throws ClassNotFoundException {
		invoke(new NettyIkasoaFactory(new Configurator(new KryoProtocolHandlerImpl<>())), ServerUtil.getNewPort());
	}

	@Test
	public void testDefaultXmlService() throws ClassNotFoundException {
		Configurator configurator = new Configurator();
		configurator.setThriftServerConfiguration(thriftServerConfiguration);
		configurator.setProtocolHandler(new XmlProtocolHandlerImpl<>());
		invoke(new DefaultIkasoaFactory(configurator), ServerUtil.getNewPort());
		// 测试NIO方式
		configurator.setNonBlockingIO(true);
		invoke(new DefaultIkasoaFactory(configurator), ServerUtil.getNewPort());
	}

	@Test
	public void testNettyXmlService() throws ClassNotFoundException {
		invoke(new NettyIkasoaFactory(new Configurator(new XmlProtocolHandlerImpl<>())), ServerUtil.getNewPort());
	}

	@Test
	public void testDefaultSerializableService() throws ClassNotFoundException {
		Configurator configurator = new Configurator();
		configurator.setThriftServerConfiguration(thriftServerConfiguration);
		configurator.setProtocolHandler(new SerializableProtocolHandlerImpl<>());
		invoke(new DefaultIkasoaFactory(configurator), ServerUtil.getNewPort());
		// 测试NIO方式
		configurator.setNonBlockingIO(true);
		invoke(new DefaultIkasoaFactory(configurator), ServerUtil.getNewPort());
	}

	@Test
	public void testNettySerializableService() throws ClassNotFoundException {
		invoke(new NettyIkasoaFactory(new Configurator(new SerializableProtocolHandlerImpl<>())),
				ServerUtil.getNewPort());
	}

	private void invoke(IkasoaFactory ikasoaFactory, int port) {
		try {

			// 获取Ikasoa服务
			List<ImplWrapper> sList = ListUtil.newArrayList(2);
			sList.add(new ImplWrapper(ExampleServiceImpl.class));
			sList.add(new ImplWrapper(ExampleChildServiceImpl.class));
			IkasoaServer ikasoaServer = ikasoaFactory.getIkasoaServer(sList, port);

			// 启动服务
			ikasoaServer.run();

			// 启动后等待一会儿
			Thread.sleep(1000);
			if (!ikasoaServer.isServing())
				Thread.sleep(1000);

			// 客户端获取远程接口实现
			ExampleService es = ikasoaFactory.getInstance(ExampleService.class,
					new ServerInfoWrapper("localhost", port));
			// 实例化一个本地接口实现
			ExampleService es2 = new ExampleServiceImpl();

			// 测试远程接口与本地接口调用结果是否一致
			assertEquals(es.findVO(4).getId(), es2.findVO(4).getId());
			assertEquals(es.getVOList().get(0).getString(), es2.getVOList().get(0).getString());
			assertEquals(es.getVOList().get(1).getEvo().getString(), es2.getVOList().get(1).getEvo().getString());
			assertEquals(es.getVOList().get(2).getString(), es2.getVOList().get(2).getString());
			assertEquals(es.getBoolean(), es2.getBoolean());
			assertEquals(es.getBoolean2(), es2.getBoolean2());
			assertEquals(es.getDouble(123), es2.getDouble(123));
			assertEquals(es.testByStrings("sulei")[0], es2.testByStrings("sulei")[0]);
			assertEquals(es.testByInts(new Integer[] { 1, 2, 2 }), es2.testByInts(new Integer[] { 1, 2, 2 }));
			Map<String, ExampleVO> map = MapUtil.newHashMap();
			map.put("sl", new ExampleVO(1, "slslsl"));
			assertEquals(es.getMap(0, map).get("sl").getString(), es2.getMap(0, map).get("sl").getString());
			es.tVoid();
			assertEquals("value", es.testContainerType().get(0).get("key"));
			assertEquals("oooo", es.testContainerType2().get(0).get(0));
			try {
				es.tInvalid();
			} catch (Exception e) {
				System.out.println(e);
			}

			// 测试接口实现继承
			ExampleChildService childEs = ikasoaFactory.getInstance(ExampleChildService.class,
					new ServerInfoWrapper("localhost", port));
			assertTrue(childEs.helloxx());
			assertFalse(childEs.helloxxx());

			// 测试文件下载
			// long startTime = System.currentTimeMillis();
			// long endTime = System.currentTimeMillis();
			// int ch = 0;
			// try (InputStream is = StreamUtil.bytesToInputStream(es.down());
			// FileOutputStream fos = new FileOutputStream("C:/2.jpg")) {
			// while ((ch = is.read()) != -1) {
			// fos.write(ch);
			// }
			// } catch (IOException e1) {
			// e1.printStackTrace();
			// }
			// System.out.println("下载耗时：" + (endTime - startTime) + "ms");

			// 停止服务
			ikasoaServer.stop();

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}
