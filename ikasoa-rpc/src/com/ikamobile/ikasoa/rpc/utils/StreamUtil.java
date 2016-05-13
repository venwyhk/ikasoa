package com.ikamobile.ikasoa.rpc.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 流处理工具
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class StreamUtil {

	public static byte[] inputStreamToBytes(InputStream is) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			int i = -1;
			while ((i = is.read()) != -1) {
				baos.write(i);
			}
			return baos.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static InputStream bytesToInputStream(byte[] bytes) {
		return new ByteArrayInputStream(bytes);
	}

}
