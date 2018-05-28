package com.ikasoa.rpc.client;

import com.ikasoa.rpc.BaseGetService;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 无效服务实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@NoArgsConstructor
@Slf4j
public class InvalidGetService<T, R> implements BaseGetService<T, R> {

	@Override
	public R get(T arg) throws Throwable {
		log.warn("Invalid rpc service call !");
		return null;
	}

}
