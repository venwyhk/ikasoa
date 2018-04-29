package com.ikasoa.rpc.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ikasoa.rpc.BaseGetService;
import com.ikasoa.rpc.BaseGetServiceFactory;

import lombok.NoArgsConstructor;

/**
 * 无效服务实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@NoArgsConstructor
public class InvalidGetService<T, R> implements BaseGetService<T, R> {

	private static final Logger LOG = LoggerFactory.getLogger(BaseGetServiceFactory.class);

	@Override
	public R get(T arg) throws Throwable {
		LOG.warn("Invalid rpc service call !");
		return null;
	}

}
