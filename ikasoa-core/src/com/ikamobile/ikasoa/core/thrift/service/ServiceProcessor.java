package com.ikamobile.ikasoa.core.thrift.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.thrift.ProcessFunction;
import org.apache.thrift.TBase;
import org.apache.thrift.TBaseProcessor;
import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ikamobile.ikasoa.core.STException;
import com.ikamobile.ikasoa.core.thrift.service.base.ArgsThriftBase;
import com.ikamobile.ikasoa.core.thrift.service.base.ResultThriftBase;

/**
 * 通用服务处理器
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.2
 */
public class ServiceProcessor extends TBaseProcessor<Service> implements TProcessor {

	private static final String FUNCTION_NAME = "get";

	@SuppressWarnings("rawtypes")
	public ServiceProcessor(Service GeneralService) {
		super(GeneralService, getProcessMap(new HashMap<String, ProcessFunction<Service, ? extends TBase>>()));
	}

	@SuppressWarnings("rawtypes")
	private static Map<String, ProcessFunction<Service, ? extends TBase>> getProcessMap(
			Map<String, ProcessFunction<Service, ? extends TBase>> processMap) {
		processMap.put(FUNCTION_NAME, new GetProcessFunction());
		return processMap;
	}

	private static class GetProcessFunction extends ProcessFunction<Service, ArgsThriftBase> {

		private static final Logger LOG = LoggerFactory.getLogger(GetProcessFunction.class);

		public GetProcessFunction() {
			super(FUNCTION_NAME);
		}

		public ArgsThriftBase getEmptyArgsInstance() {
			return new ArgsThriftBase();
		}

		protected boolean isOneway() {
			return false;
		}

		public ResultThriftBase getResult(Service service, ArgsThriftBase args) throws TException {
			LOG.debug("Args is : " + args.getStr());
			try {
				// 在这里执行服务端实现类的'get'方法
				return new ResultThriftBase(service.get(args.getStr()));
			} catch (STException e) {
				throw new TException(e);
			}
		}
	}

}
