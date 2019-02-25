package com.ikasoa.core;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.junit.Ignore;

import junit.framework.TestCase;

@Ignore
public class ServerTestCase extends TestCase {

	protected String getSslUrlFileString(String fileName) {
		URL url = this.getClass().getResource(fileName);
		return url == null ? null : url.getFile();
	}

	protected void waiting() {
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			fail();
		}
	}

}
