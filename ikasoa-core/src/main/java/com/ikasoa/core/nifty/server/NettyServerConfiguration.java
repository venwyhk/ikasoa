package com.ikasoa.core.nifty.server;

import lombok.AllArgsConstructor;
import lombok.Data;

import org.jboss.netty.util.Timer;

import java.util.Map;
import java.util.concurrent.ExecutorService;

@Data
@AllArgsConstructor
public class NettyServerConfiguration {
	private final Map<String, Object> bootstrapOptions;
	private final Timer timer;
	private final ExecutorService bossExecutor;
	private final int bossThreadCount;
	private final ExecutorService workerExecutor;
	private final int workerThreadCount;
}
