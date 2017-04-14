package com.ikasoa.rpc.handler.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ikasoa.core.utils.StringUtil;
import com.ikasoa.rpc.handler.ClientInvocationContext;
import com.ikasoa.rpc.handler.ClientInvocationHandler;

/**
 * 日志输出拦截器实现(测试用)
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class LoggerClientInvocationHandlerImpl implements ClientInvocationHandler {

	private static final Logger LOG = LoggerFactory.getLogger(LoggerClientInvocationHandlerImpl.class);

	private long time = 0;

	@Override
	public ClientInvocationContext before(ClientInvocationContext context) {
		time = System.currentTimeMillis();
		StringBuilder sb = new StringBuilder("开始调用远程接口. ");
		if (StringUtil.isNotEmpty(context.getServiceKey())) {
			sb.append("接口名: " + context.getServiceKey());
		}
		sb.append(".");
		LOG.info(sb.toString());
		return context;
	}

	@Override
	public ClientInvocationContext invoke(ClientInvocationContext context) {
		return context;
	}

	@Override
	public void after(ClientInvocationContext context) {
		StringBuilder sb = new StringBuilder("远程接口调用完成. ");
		if (StringUtil.isNotEmpty(context.getServiceKey())) {
			sb.append("接口名: ").append(context.getServiceKey()).append(", ");
		}
		sb.append("耗时: ").append(System.currentTimeMillis() - time).append("毫秒 .");
		LOG.info(sb.toString());
	}

	@Override
	public void exception(ClientInvocationContext context, Throwable throwable) {
		StringBuilder sb = new StringBuilder("远程接口调用异常. ");
		if (StringUtil.isNotEmpty(context.getServiceKey())) {
			sb.append("接口名: ").append(context.getServiceKey()).append(", ");
		}
		sb.append("异常信息: ").append(throwable.getMessage());
		LOG.info(sb.toString());
	}

}
