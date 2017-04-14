package com.ikasoa.rpc.handler.impl;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.ikasoa.rpc.handler.ProtocolHandler;
import com.ikasoa.rpc.handler.ReturnData;

/**
 * 转换协议处理器XML实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class XmlProtocolHandlerImpl<T1, T2> implements ProtocolHandler<T1, T2> {

	private ReturnData resultData;

	private final static String E = "<!--E-->";

	private final static String VOID = "_VOID_";

	public XmlProtocolHandlerImpl(ReturnData resultData) {
		this.resultData = resultData;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T1 strToArg(String str) {
		return (T1) parserXML(str);
	}

	@Override
	public String argToStr(T1 arg) {
		return formatXML(arg);
	}

	@Override
	public String resultToStr(T2 result) {
		if (result instanceof Throwable) {
			return new StringBuilder(E).append(formatXML(result)).toString();
		} else {
			if (result != null) {
				return formatXML(result);
			} else {
				return VOID;
			}
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public T2 strToResult(String str) {
		if (VOID.equals(str)) {
			return null;
		}
		return (T2) parserXML(str);
	}

	@Override
	public Throwable strToThrowable(String str) {
		String[] strs = str.split(E);
		if (strs.length == 2 && "".equals(strs[0])) {
			return parserXML(strs[1]);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private <T> T parserXML(String xml) {
		ByteArrayInputStream in = new ByteArrayInputStream(xml.getBytes());
		XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(in));
		decoder.close();
		return (T) decoder.readObject();
	}

	@Override
	public ReturnData getReturnData() {
		return resultData;
	}

	private <T> String formatXML(T entity) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(out));
		encoder.writeObject(entity);
		encoder.close();
		return out.toString();
	}

}
