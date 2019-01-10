package com.ikasoa.rpc.handler.impl;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

import com.esotericsoftware.minlog.Log;
import com.ikasoa.rpc.handler.ProtocolHandler;
import com.ikasoa.rpc.handler.ReturnData;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 转换协议处理器XML实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@AllArgsConstructor
@NoArgsConstructor
public class XmlProtocolHandlerImpl<T, R> implements ProtocolHandler<T, R> {

	private ReturnData resultData;

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
		return result instanceof Throwable ? new StringBuilder(String.valueOf(E)).append(formatXML(result)).toString()
				: Optional.ofNullable(result).map(this::formatXML).orElse(String.valueOf(V));
	}

	@Override
	@SuppressWarnings("unchecked")
	public R strToResult(String str) {
		return String.valueOf(V).equals(str) ? null : (R) parserXML(str);
	}

	@Override
	public Throwable strToThrowable(String str) {
		String[] strs = str.split(String.valueOf(E));
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
			return String.valueOf(V);
		}
	}

}
