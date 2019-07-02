package com.ikasoa.core.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

/**
 * 流处理工具
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.6.2
 */
@UtilityClass
public class StreamUtil {

	@SneakyThrows
	public static byte[] inputStreamToBytes(InputStream is) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			int i;
			while ((i = is.read()) != -1)
				baos.write(i);
			return baos.toByteArray();
		}
	}

	public static InputStream bytesToInputStream(byte[] bytes) {
		return new ByteArrayInputStream(bytes);
	}

	@SneakyThrows
	public static byte[] objectToBytes(Object obj) {
		byte[] bytes = null;
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(bos)) {
			oos.writeObject(obj);
			oos.flush();
			bytes = bos.toByteArray();
		}
		return bytes;
	}

	@SneakyThrows
	public static Object bytesToObject(byte[] bytes) {
		Object obj = null;
		try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
				ObjectInputStream ois = new ObjectInputStream(bis)) {
			obj = ois.readObject();
		}
		return obj;
	}

}
