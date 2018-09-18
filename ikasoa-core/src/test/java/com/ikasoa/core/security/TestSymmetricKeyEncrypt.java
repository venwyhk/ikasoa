package com.ikasoa.core.security;

import org.junit.Test;

import com.ikasoa.core.TestBase;
import com.ikasoa.core.security.impl.DESEncryptImpl;
import com.ikasoa.core.security.impl.RC4EncryptImpl;

/**
 * 对称加密单元测试
 */
public class TestSymmetricKeyEncrypt extends TestBase {

	@Test
	public void testDES() {
		SymmetricKeyEncrypt encrypt = new DESEncryptImpl();
		try {
			assertEquals(TEST_STRING, new String(encrypt.decrypt(encrypt.encrypt(TEST_STRING, TEST_KEY8), TEST_KEY8)));
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testRC4() {
		SymmetricKeyEncrypt encrypt = new RC4EncryptImpl();
		try {
			assertEquals(TEST_STRING, new String(encrypt.decrypt(encrypt.encrypt(TEST_STRING, TEST_KEY8), TEST_KEY8)));
		} catch (Exception e) {
			fail();
		}
	}

}
