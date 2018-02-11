package com.ikasoa.rpc.handler.impl;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.esotericsoftware.minlog.Log;
import com.ikasoa.rpc.handler.ProtocolHandler;
import com.ikasoa.rpc.handler.ReturnData;

/**
 * 转换协议处理器XML实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class XmlProtocolHandlerImpl<T, R> implements ProtocolHandler<T, R> {

	private ReturnData resultData;

	/**
	 * 异常标识符
	 */
	private final static String E = "<!--E-->";

	/**
	 * 空标识符
	 */
	private final static String VOID = "_VOID_";

	public XmlProtocolHandlerImpl(ReturnData resultData) {
		this.resultData = resultData;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T strToArg(String str) {
		return (T) parserXML(str);
	}

	@Override
	public String argToStr(T arg) {
		return formatXML(arg);
	}

	@Override
	public String resultToStr(R result) {
		return result instanceof Throwable ? new StringBuilder(E).append(formatXML(result)).toString()
				: result != null ? formatXML(result) : VOID;
	}

	@Override
	@SuppressWarnings("unchecked")
	public R strToResult(String str) {
		return VOID.equals(str) ? null : (R) parserXML(str);
	}

	@Override
	public Throwable strToThrowable(String str) {
		String[] strs = str.split(E);
		return strs.length == 2 && strs[0].length() == 0 ? parserXML(strs[1]) : null;
	}

	@SuppressWarnings("unchecked")
	private <E> E parserXML(String xml) {
		XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new ByteArrayInputStream(xml.getBytes())));
		decoder.close();
		return (E) decoder.readObject();
	}

	@Override
	public ReturnData getReturnData() {
		return resultData;
	}

	private <E> String formatXML(E entity) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				BufferedOutputStream bos = new BufferedOutputStream(baos)) {
			XMLEncoder encoder = new XMLEncoder(bos);
			encoder.writeObject(entity);
			encoder.close();
			return baos.toString();
		} catch (IOException e) {
			Log.error(e.getMessage());
			return VOID;
		}
	}

}
