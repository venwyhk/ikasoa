package com.ikasoa.rpc.handler;

import org.junit.Test;

import com.ikasoa.rpc.handler.impl.KryoProtocolHandlerImpl;
import com.ikasoa.rpc.handler.impl.SerializableProtocolHandlerImpl;
import com.ikasoa.rpc.handler.impl.XmlProtocolHandlerImpl;
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
			test(new XmlProtocolHandlerImpl<>());
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testKryoProtocolHandlerImpl() {
		try {
			test(new KryoProtocolHandlerImpl<>());
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testSerializableProtocolHandlerImpl() {
		try {
			test(new SerializableProtocolHandlerImpl<>());
		} catch (Exception e) {
			fail();
		}
	}

	private void test(ProtocolHandler<?, ?> protocolHandler) throws ClassNotFoundException {
		ProtocolHandlerFactory<Object[], ExampleVO> chf = new ProtocolHandlerFactory<>();
		ReturnData rd = new ReturnData(ExampleVO.class);
		ProtocolHandler<Object[], ExampleVO> ch = chf.getProtocolHandler(rd, protocolHandler);

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
