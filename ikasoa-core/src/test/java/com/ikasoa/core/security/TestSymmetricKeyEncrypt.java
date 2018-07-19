package com.ikasoa.core.security;

import org.junit.Test;

import com.ikasoa.core.security.impl.DESEncryptImpl;
import com.ikasoa.core.security.impl.RC4EncryptImpl;

import junit.framework.TestCase;

/**
 * 对称加密单元测试
 */
public class TestSymmetricKeyEncrypt extends TestCase {

	private static String key = "12345678";

	private static String testString = "12345678abcdefgABCDEFG~!@#$%^&*()_+";

	@Test
	public void testDES() {
		SymmetricKeyEncrypt encrypt = new DESEncryptImpl();
		try {
			assertEquals(testString, new String(encrypt.decrypt(encrypt.encrypt(testString, key), key)));
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testRC4() {
		SymmetricKeyEncrypt encrypt = new RC4EncryptImpl();
		try {
			assertEquals(testString, new String(encrypt.decrypt(encrypt.encrypt(testString, key), key)));
		} catch (Exception e) {
			fail();
		}
	}

}
