package com.ikasoa.core.thrift.service;

import java.util.Map;

import org.apache.thrift.ProcessFunction;
import org.apache.thrift.TBase;
import org.apache.thrift.TBaseProcessor;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransportException;

import com.ikasoa.core.IkasoaException;
import com.ikasoa.core.thrift.service.base.ArgsThriftBase;
import com.ikasoa.core.thrift.service.base.ResultThriftBase;
import com.ikasoa.core.utils.MapUtil;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 通用服务处理器
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.2
 */
public class ServiceProcessor extends TBaseProcessor<Service> implements Processor {

	public ServiceProcessor(Service service) {
		super(service, getProcessMap(MapUtil.newHashMap()));
	}

	@SuppressWarnings("rawtypes")
	private static Map<String, ProcessFunction<Service, ? extends TBase>> getProcessMap(
			Map<String, ProcessFunction<Service, ? extends TBase>> processMap) {
		processMap.put(FUNCTION_NAME, new GetProcessFunction());
		return processMap;
	}

	@Override
	public boolean process(TProtocol in, TProtocol out) throws TException {
		try {
			return super.process(in, out);
		} catch (TTransportException e) {
			return false; // 如果连接中断就停止服务但不抛出异常
		}
	}

	@Slf4j
	private static class GetProcessFunction extends ProcessFunction<Service, ArgsThriftBase> {

		public GetProcessFunction() {
			super(FUNCTION_NAME);
		}

		public ArgsThriftBase getEmptyArgsInstance() {
			return new ArgsThriftBase();
		}

		protected boolean isOneway() {
			return false;
		}

		@SneakyThrows(IkasoaException.class)
		public ResultThriftBase getResult(Service service, ArgsThriftBase args) throws TException {
			log.debug("Args is : {}", args.getStr());
			// 在这里执行服务端实现类的'get'方法
			return new ResultThriftBase(service.get(args.getStr()));
		}
	}

}
