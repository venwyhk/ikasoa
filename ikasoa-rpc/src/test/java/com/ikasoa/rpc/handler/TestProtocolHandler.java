package com.ikasoa.rpc.handler;

import org.junit.Test;

import com.ikasoa.rpc.service.ExampleVO;

import junit.framework.TestCase;

public class TestProtocolHandler extends TestCase {

	private int testId = 1;
	private String testStr = "Protocol测试";
	private ExampleVO evo = new ExampleVO(testId, testStr);

	@Test
	public void testJsonProtocolHandlerImpl() {
		try {
			test(null);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testXmlProtocolHandlerImpl() {
		try {
			test(Class.forName("com.ikasoa.rpc.handler.impl.XmlProtocolHandlerImpl"));
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testKryoProtocolHandlerImpl() {
		try {
			test(Class.forName("com.ikasoa.rpc.handler.impl.KryoProtocolHandlerImpl"));
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testSerializableProtocolHandlerImpl() {
		try {
			test(Class.forName("com.ikasoa.rpc.handler.impl.SerializableProtocolHandlerImpl"));
		} catch (Exception e) {
			fail();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void test(Class protocolHandlerClass) throws ClassNotFoundException {
		ProtocolHandlerFactory<Object[], ExampleVO> chf = new ProtocolHandlerFactory<>();
		ReturnData rd = new ReturnData(ExampleVO.class);
		ProtocolHandler<Object[], ExampleVO> ch = chf.getProtocolHandler(rd, protocolHandlerClass);

		String as = ch.argToStr(new Object[] { evo });
		Object[] evoo = ch.strToArg(as);
		ExampleVO aevo = (ExampleVO) evoo[0];
		assertNotNull(aevo);
		assertEquals(aevo.getId(), testId);
		assertEquals(aevo.getString(), testStr);

		String rs = ch.resultToStr(evo);
		ExampleVO revo = ch.strToResult(rs);
		assertNotNull(revo);
		assertEquals(revo.getId(), testId);
		assertEquals(revo.getString(), testStr);
	}

}
