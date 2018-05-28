package com.ikasoa.springboot.runner;

import java.util.Optional;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import com.ikasoa.rpc.IkasoaException;
import com.ikasoa.rpc.IkasoaServer;

import lombok.extern.slf4j.Slf4j;

/**
 * IKASOA服务器Runner类
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@Slf4j
public abstract class IkasoaServerRunner implements CommandLineRunner {

	@Autowired
	protected IkasoaServer server;

	protected long getRunWaitTime() throws IkasoaException {
		return 1000L;
	};

	protected void complete(String... args) throws IkasoaException {
	};

	protected void fail(String... args) throws IkasoaException {
	};

	@Override
	public void run(String... args) throws IkasoaException, InterruptedException {
		if (server == null)
			throw new IkasoaException("Ikasoa server initialize failed !");
		server.run();
		Thread.sleep(getRunWaitTime());
		if (server.isServing()) {
			log.info("Ikasoa server startup success . ({})", server.getServerName());
			complete(args);
		} else {
			log.warn("Ikasoa server startup failed ! ({})", server.getServerName());
			fail(args);
		}
	}

	@PreDestroy
	public void destory() {
		Optional.ofNullable(server).ifPresent(s -> s.stop());
	}

}
