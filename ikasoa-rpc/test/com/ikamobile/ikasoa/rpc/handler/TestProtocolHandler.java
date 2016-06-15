package com.ikamobile.ikasoa.rpc.handler;

import org.junit.Test;

import com.ikamobile.ikasoa.rpc.handler.ProtocolHandlerFactory.ProtocolType;
import com.ikamobile.ikasoa.rpc.service.ExampleVO;

import junit.framework.TestCase;

public class TestProtocolHandler extends TestCase {

	private int testId = 1;
	private String testStr = "Protocol测试";
	private ExampleVO evo = new ExampleVO(testId, testStr);

	@Test
	public void testJsonProtocolHandlerImpl() {

		ProtocolHandlerFactory<Object[], ExampleVO> chf = new ProtocolHandlerFactory<Object[], ExampleVO>();
		ReturnData rd = new ReturnData(ExampleVO.class);
		ProtocolHandler<Object[], ExampleVO> ch = chf.getProtocolHandler(rd);

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

	@Test
	public void testXmlProtocolHandlerImpl() {

		ProtocolHandlerFactory<Object[], ExampleVO> chf = new ProtocolHandlerFactory<Object[], ExampleVO>();
		ReturnData rd = new ReturnData(ExampleVO.class);
		ProtocolHandler<Object[], ExampleVO> ch = chf.getProtocolHandler(rd, ProtocolType.XML);

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

	@Test
	public void testKryoProtocolHandlerImpl() {

		ProtocolHandlerFactory<Object[], ExampleVO> chf = new ProtocolHandlerFactory<Object[], ExampleVO>();
		ReturnData rd = new ReturnData(ExampleVO.class);
		ProtocolHandler<Object[], ExampleVO> ch = chf.getProtocolHandler(rd, ProtocolType.KRYO);

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
