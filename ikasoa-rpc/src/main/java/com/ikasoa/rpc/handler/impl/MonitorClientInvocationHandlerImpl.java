package com.ikasoa.rpc.handler.impl;

import java.util.Map;

import com.ikasoa.core.utils.MapUtil;
import com.ikasoa.core.utils.StringUtil;
import com.ikasoa.rpc.handler.ClientInvocationContext;
import com.ikasoa.rpc.handler.ClientInvocationHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * 调用时间监控拦截器实现(测试用)
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@Slf4j
public class MonitorClientInvocationHandlerImpl implements ClientInvocationHandler {

	private Map<String, Long> timeMap = MapUtil.newHashMap();

	@Override
	public ClientInvocationContext before(ClientInvocationContext context) {
		StringBuilder sb = new StringBuilder("开始调用远程接口. ");
		if (StringUtil.isNotEmpty(context.getServiceKey())) {
			sb.append("接口名: ");
			sb.append(context.getServiceKey());
		}
		sb.append(".");
		log.info(sb.toString());
		return context;
	}

	@Override
	public ClientInvocationContext invoke(ClientInvocationContext context) {
		timeMap.put(context.getUuid(), System.currentTimeMillis());
		return context;
	}

	@Override
	public void after(ClientInvocationContext context) {
		StringBuilder sb = new StringBuilder("远程接口调用完成. ");
		if (timeMap.containsKey(context.getUuid())) {
			if (StringUtil.isNotEmpty(context.getServiceKey()))
				sb.append(context.getUuid()).append(" ");
			sb.append("接口名: ").append(context.getServiceKey()).append(", ");
			sb.append("耗时: ").append(System.currentTimeMillis() - timeMap.get(context.getUuid())).append("毫秒 .");
			timeMap.remove(context.getUuid());
		}
		log.info(sb.toString());
	}

	@Override
	public void exception(ClientInvocationContext context, Throwable throwable) {
		StringBuilder sb = new StringBuilder("远程接口调用异常. ");
		if (StringUtil.isNotEmpty(context.getServiceKey()))
			sb.append("接口名: ").append(context.getServiceKey()).append(", ");
		sb.append("异常信息: ").append(throwable.getMessage());
		log.info(sb.toString());
	}

}
