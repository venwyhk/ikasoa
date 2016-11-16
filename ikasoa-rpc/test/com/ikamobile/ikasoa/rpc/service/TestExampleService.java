package com.ikamobile.ikasoa.rpc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.thrift.server.TThreadPoolServer;
import org.junit.Before;
import org.junit.Test;
import com.ikamobile.ikasoa.rpc.IkasoaServer;
import com.ikamobile.ikasoa.rpc.ImplClsCon;
import com.ikamobile.ikasoa.rpc.NettyIkasoaFactory;
import com.ikamobile.ikasoa.core.thrift.server.ServerArgsAspect;
import com.ikamobile.ikasoa.core.thrift.server.ThriftServerConfiguration;
import com.ikamobile.ikasoa.rpc.Configurator;
import com.ikamobile.ikasoa.rpc.DefaultIkasoaFactory;
import com.ikamobile.ikasoa.rpc.IkasoaFactory;

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
			public TThreadPoolServer.Args TThreadPoolServerArgsAspect(TThreadPoolServer.Args args) {
				args.stopTimeoutVal = 1;
				return args;
			}
		});
	}

	@Test
	public void testDefaultService() {
		Configurator configurator = new Configurator();
		configurator.setThriftServerConfiguration(thriftServerConfiguration);
		invoke(new DefaultIkasoaFactory(configurator), 9991);
		// 测试NIO方式
		configurator.setNonBlockingIO(true);
		invoke(new DefaultIkasoaFactory(configurator), 9992);
	}

	@Test
	public void testNettyService() {
		invoke(new NettyIkasoaFactory(), 9993);
	}

	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void testDefaultKryoService() throws ClassNotFoundException {
		Configurator configurator = new Configurator();
		configurator.setThriftServerConfiguration(thriftServerConfiguration);
		Class protocolHandlerClass = Class.forName("com.ikamobile.ikasoa.rpc.handler.impl.KryoProtocolHandlerImpl");
		configurator.setProtocolHandlerClass(protocolHandlerClass);
//		invoke(new DefaultIkasoaFactory(configurator), 9994);
		// 测试NIO方式
		configurator.setNonBlockingIO(true);
		invoke(new DefaultIkasoaFactory(configurator), 9995);
	}

	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void testNettyKryoService() throws ClassNotFoundException {
		Class protocolHandlerClass = Class.forName("com.ikamobile.ikasoa.rpc.handler.impl.KryoProtocolHandlerImpl");
		invoke(new NettyIkasoaFactory(new Configurator(protocolHandlerClass)), 9996);
	}

	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void testDefaultXmlService() throws ClassNotFoundException {
		Configurator configurator = new Configurator();
		configurator.setThriftServerConfiguration(thriftServerConfiguration);
		Class protocolHandlerClass = Class.forName("com.ikamobile.ikasoa.rpc.handler.impl.XmlProtocolHandlerImpl");
		configurator.setProtocolHandlerClass(protocolHandlerClass);
//		invoke(new DefaultIkasoaFactory(configurator), 9997);
		// 测试NIO方式
		configurator.setNonBlockingIO(true);
		invoke(new DefaultIkasoaFactory(configurator), 9998);
	}

	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void testNettyXmlService() throws ClassNotFoundException {
		Class protocolHandlerClass = Class.forName("com.ikamobile.ikasoa.rpc.handler.impl.XmlProtocolHandlerImpl");
		invoke(new NettyIkasoaFactory(new Configurator(protocolHandlerClass)), 9999);
	}

	private void invoke(IkasoaFactory ikasoaFactory, int port) {
		try {

			// 获取Ikasoa服务
			List<ImplClsCon> sList = new ArrayList<>();
			sList.add(new ImplClsCon(ExampleServiceImpl.class));
			sList.add(new ImplClsCon(ExampleChildServiceImpl.class));
			IkasoaServer ikasoaServer = ikasoaFactory.getIkasoaServer(sList, port);

			// 启动服务
			ikasoaServer.run();

			// 启动后等待半秒
			Thread.sleep(500);

			// 客户端获取远程接口实现
			ExampleService es = ikasoaFactory.getIkasoaClient(ExampleService.class, "localhost", port);
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
			Map<String, ExampleVO> map = new HashMap<>();
			map.put("sl", new ExampleVO(1, "slslsl"));
			assertEquals(es.getMap(0, map).get("sl").getString(), es2.getMap(0, map).get("sl").getString());
			es.tVoid();
			
			// 测试接口实现继承
			ExampleChildService childEs = ikasoaFactory.getIkasoaClient(ExampleChildService.class, "localhost", port);
			assertEquals(childEs.helloxx(), Boolean.TRUE);
			assertEquals(childEs.helloxxx(), Boolean.FALSE);

			// 测试文件下载
			// long startTime = System.currentTimeMillis();
			// InputStream is = StreamUtil.bytesToInputStream(es.down());
			// long endTime = System.currentTimeMillis();
			// FileOutputStream fos = new FileOutputStream("C:/2.jpg");
			// int ch = 0;
			// try {
			// while ((ch = is.read()) != -1) {
			// fos.write(ch);
			// }
			// } catch (IOException e1) {
			// e1.printStackTrace();
			// } finally {
			// fos.close();
			// is.close();
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
