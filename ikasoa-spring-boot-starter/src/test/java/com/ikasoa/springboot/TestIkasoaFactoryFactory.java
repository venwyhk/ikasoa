package com.ikasoa.springboot;

import org.junit.Test;

import com.ikasoa.rpc.Configurator;
import com.ikasoa.rpc.DefaultIkasoaFactory;
import com.ikasoa.rpc.NettyIkasoaFactory;

import junit.framework.TestCase;

public class TestIkasoaFactoryFactory extends TestCase {

	@Test
	public void testGetIkasoaDefaultFactory() {
		IkasoaFactoryFactory factory1 = new IkasoaFactoryFactory();
		assertNotNull(factory1.getIkasoaDefaultFactory());
		IkasoaFactoryFactory factory2 = new IkasoaFactoryFactory(new Configurator());
		assertNotNull(factory2.getIkasoaDefaultFactory());
		assertTrue(factory1.getIkasoaDefaultFactory() instanceof DefaultIkasoaFactory
				&& factory2.getIkasoaDefaultFactory() instanceof DefaultIkasoaFactory);

	}

	@Test
	public void testGetIkasoaNettyFactory() {
		IkasoaFactoryFactory factory1 = new IkasoaFactoryFactory();
		assertNotNull(factory1.getIkasoaNettyFactory());
		IkasoaFactoryFactory factory2 = new IkasoaFactoryFactory(new Configurator());
		assertNotNull(factory2.getIkasoaNettyFactory());
		assertTrue(factory1.getIkasoaNettyFactory() instanceof NettyIkasoaFactory
				&& factory2.getIkasoaNettyFactory() instanceof NettyIkasoaFactory);
	}

}
