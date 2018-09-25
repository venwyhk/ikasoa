package com.ikasoa.core.security;

import org.junit.Test;

import com.ikasoa.core.TestConstants;
import com.ikasoa.core.security.impl.DESEncryptImpl;
import com.ikasoa.core.security.impl.RC4EncryptImpl;

import junit.framework.TestCase;

/**
 * 对称加密单元测试
 */
public class TestSymmetricKeyEncrypt extends TestCase {

	@Test
	public void testDES() {
		SymmetricKeyEncrypt encrypt = new DESEncryptImpl();
		try {
			assertEquals(TestConstants.TEST_STRING,
					new String(encrypt.decrypt(encrypt.encrypt(TestConstants.TEST_STRING, TestConstants.TEST_KEY8),
							TestConstants.TEST_KEY8)));
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testRC4() {
		SymmetricKeyEncrypt encrypt = new RC4EncryptImpl();
		try {
			assertEquals(TestConstants.TEST_STRING,
					new String(encrypt.decrypt(encrypt.encrypt(TestConstants.TEST_STRING, TestConstants.TEST_KEY8),
							TestConstants.TEST_KEY8)));
		} catch (Exception e) {
			fail();
		}
	}

}
