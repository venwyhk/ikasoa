package com.ikasoa.core.security;

import org.junit.Test;

import com.ikasoa.core.TestConstants;
import com.ikasoa.core.security.impl.CaesarEncryptImpl;
import com.ikasoa.core.security.impl.DESEncryptImpl;
import com.ikasoa.core.security.impl.RC4EncryptImpl;

import junit.framework.TestCase;

/**
 * 对称加密单元测试
 */
public class TestSymmetricKeyEncrypt extends TestCase {

	@Test
	public void testDES() {
		test(new DESEncryptImpl());
	}

	@Test
	public void testRC4() {
		test(new RC4EncryptImpl());
	}

	@Test
	public void testCaesar() {
		test(new CaesarEncryptImpl());
	}

	private void test(SymmetricKeyEncrypt encrypt) {
		try {
			assertEquals(TestConstants.TEST_STRING,
					new String(encrypt.decrypt(encrypt.encrypt(TestConstants.TEST_STRING, TestConstants.TEST_KEY8),
							TestConstants.TEST_KEY8)));
		} catch (Exception e) {
			fail();
		}
	}

}
