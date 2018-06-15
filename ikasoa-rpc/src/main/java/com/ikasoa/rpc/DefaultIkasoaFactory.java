package com.ikasoa.rpc;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.thrift.TProcessor;

import com.ikasoa.core.IkasoaException;
import com.ikasoa.core.loadbalance.LoadBalance;
import com.ikasoa.core.loadbalance.ServerInfo;
import com.ikasoa.core.thrift.GeneralFactory;
import com.ikasoa.core.thrift.client.ThriftClient;
import com.ikasoa.core.thrift.client.ThriftClientConfiguration;
import com.ikasoa.core.thrift.server.ThriftServer;
import com.ikasoa.core.thrift.server.ThriftServerConfiguration;
import com.ikasoa.core.thrift.service.Service;
import com.ikasoa.core.utils.StringUtil;
import com.ikasoa.rpc.annotation.IkasoaService;
import com.ikasoa.rpc.annotation.Invalid;
import com.ikasoa.rpc.handler.ProtocolHandlerFactory;
import com.ikasoa.rpc.handler.ReturnData;
import com.ikasoa.rpc.service.IkasoaServerService;
import com.ikasoa.rpc.service.impl.IkasoaServerImpl;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认IKASOA服务工厂
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@NoArgsConstructor
@Slf4j
public class DefaultIkasoaFactory extends GeneralFactory implements IkasoaFactory {

	private Configurator configurator = new Configurator();

	public DefaultIkasoaFactory(ThriftServerConfiguration thriftServerConfiguration) {
		super.thriftServerConfiguration = thriftServerConfiguration;
		configurator.setThriftServerConfiguration(thriftServerConfiguration);
	}

	public DefaultIkasoaFactory(ThriftClientConfiguration thriftClientConfiguration) {
		super.thriftClientConfiguration = thriftClientConfiguration;
		configurator.setThriftClientConfiguration(thriftClientConfiguration);
	}

	public DefaultIkasoaFactory(Configurator configurator) {
		if (configurator != null) {
			this.configurator = configurator;
			if (configurator.getThriftServerConfiguration() != null)
				thriftServerConfiguration = configurator.getThriftServerConfiguration();
			if (configurator.getThriftClientConfiguration() != null)
				thriftClientConfiguration = configurator.getThriftClientConfiguration();
		}
	}

	public DefaultIkasoaFactory(ServerInfoWrapper siw) {
		if (siw != null && !siw.isNotNull())
			configurator = new Configurator(siw);
	}

	public <T> T getInstance(Class<T> iClass) {
		return getInstance(iClass, configurator.getServerInfoWrapper());
	}

	@SuppressWarnings("unchecked")
	public <T> T getInstance(Class<T> iClass, ServerInfoWrapper siw) {
		if (iClass == null)
			throw new IllegalArgumentException();
		if (siw == null || !siw.isNotNull())
			throw new IllegalArgumentException("'serverInfoWrapper' is exist !");
		return (T) Proxy
				.newProxyInstance(iClass.getClassLoader(),
						new Class<?>[] {
								iClass },
						(proxy, iMethod,
								args) -> getBaseGetServiceFactory()
										.getBaseGetService(
												siw.isCluster()
														? siw.getLoadBalanceClass() == null
																? getThriftClient(siw.getServerInfoList())
																: getThriftClient(siw.getServerInfoList(),
																		siw.getLoadBalanceClass(), siw.getParam())
														: getThriftClient(siw.getHost(), siw.getPort()),
												getSKey(iClass, iMethod, true), new ReturnData(iMethod))
										.get(args));
	}

	@Override
	@Deprecated
	public <T> T getIkasoaClient(Class<T> iClass, String serverHost, int serverPort) {
		return getInstance(iClass, new ServerInfoWrapper(serverHost, serverPort));
	}

	@Override
	@Deprecated
	public <T> T getIkasoaClient(Class<T> iClass, List<ServerInfo> serverInfoList) {
		return getInstance(iClass, new ServerInfoWrapper(serverInfoList));
	}

	@Override
	@Deprecated
	public <T> T getIkasoaClient(Class<T> iClass, List<ServerInfo> serverInfoList,
			Class<LoadBalance> loadBalanceClass) {
		return getInstance(iClass, new ServerInfoWrapper(serverInfoList, loadBalanceClass));
	}

	@Override
	@Deprecated
	public <T> T getIkasoaClient(Class<T> iClass, List<ServerInfo> serverInfoList, Class<LoadBalance> loadBalanceClass,
			String param) {
		return getInstance(iClass, new ServerInfoWrapper(serverInfoList, loadBalanceClass, param));
	}

	@Override
	public IkasoaServer getIkasoaServer(Class<?> implClass, int serverPort) throws RpcException {
		return getIkasoaServer(serverPort, getServiceMapByImplWrapper(new ImplWrapper(implClass)));
	}

	@Override
	public IkasoaServer getIkasoaServer(ImplWrapper implWrapper, int serverPort) throws RpcException {
		return getIkasoaServer(serverPort, getServiceMapByImplWrapper(implWrapper));
	}

	@Override
	public IkasoaServer getIkasoaServer(List<ImplWrapper> ImplWrapperList, int serverPort) throws RpcException {
		Map<String, Service> serviceMap = new HashMap<>();
		for (ImplWrapper implWrapper : ImplWrapperList)
			serviceMap.putAll(getServiceMapByImplWrapper(implWrapper));
		return getIkasoaServer(serverPort, serviceMap);
	}

	@Override
	public IkasoaServer getIkasoaServer(int serverPort, Map<String, Service> serviceMap) {
		return new IkasoaServerImpl(super.getThriftServer(serverPort, serviceMap), serviceMap);
	}

	@Override
	public Service getService(ThriftClient thriftClient) throws IkasoaException {
		return super.getService(thriftClient);
	}

	@Override
	public Service getService(ThriftClient thriftClient, String serviceName) throws IkasoaException {
		return super.getService(thriftClient, serviceName);
	}

	@Override
	public ThriftClient getThriftClient(String serverHost, int serverPort) {
		return super.getThriftClient(serverHost, serverPort);
	}

	@Override
	public ThriftClient getThriftClient(List<ServerInfo> serverInfoList) {
		return super.getThriftClient(serverInfoList);
	}

	@Override
	public ThriftServer getThriftServer(int serverPort, Service service) {
		return super.getThriftServer(serverPort, service);
	}

	@Override
	public ThriftServer getThriftServer(int serverPort, Map<String, Service> serviceMap) {
		return super.getThriftServer(serverPort, serviceMap);
	}

	@Override
	public ThriftServer getThriftServer(String serverName, int serverPort, Service service) {
		return super.getThriftServer(serverName, serverPort, service);
	}

	@Override
	public ThriftServer getThriftServer(String serverName, int serverPort, Map<String, Service> serviceMap) {
		return super.getThriftServer(serverName, serverPort, serviceMap);
	}

	@Override
	public ThriftServer getThriftServer(String serverName, int serverPort, TProcessor processor) {
		return configurator.isNonBlockingIO()
				? getNonblockingThriftServer(serverName += " (non-blocking io)", serverPort, processor)
				: super.getThriftServer(serverName, serverPort, processor);
	}

	private Map<String, Service> getServiceMapByImplWrapper(ImplWrapper implWrapper) throws RpcException {
		return getServiceMapByClass(new HashMap<String, Service>(), implWrapper.getImplClass(),
				implWrapper.getImplObject(), implWrapper.getImplClass());
	}

	private Map<String, Service> getServiceMapByClass(Map<String, Service> serviceMap, Class<?> implClass,
			Object implObject, Class<?> superClass) throws RpcException {
		if (implClass == null)
			throw new IllegalArgumentException("Implement 'class' is not null !");
		if (superClass == null)
			throw new IllegalArgumentException("Implement 'superClass' is not null !");
		if (implClass.getInterfaces().length == 0)
			log.warn("Class ({}) is not this interface implement class , Will ignore .", implClass.getName());
		for (Class<?> iClass : superClass.getInterfaces())
			buildService(serviceMap, iClass, implClass, implObject);
		if (superClass.getSuperclass() != null && !Object.class.equals(superClass.getSuperclass()))
			serviceMap.putAll(getServiceMapByClass(serviceMap, implClass, implObject, superClass.getSuperclass()));
		return serviceMap;
	}

	@SneakyThrows
	private void buildService(Map<String, Service> serviceMap, Class<?> iClass, Class<?> implClass, Object implObject) {
		if (implObject == null)
			implObject = implClass.newInstance();
		ProtocolHandlerFactory<Object[], Object> protocolHandlerFactory = new ProtocolHandlerFactory<>();
		for (Method implMethod : implClass.getMethods()) {
			boolean isValidMethod = Boolean.FALSE;
			// 对hashCode和toString两个方法做特殊处理
			if ("hashCode".equals(implMethod.getName()) || "toString".equals(implMethod.getName()))
				isValidMethod = Boolean.TRUE;
			else
				// 过滤掉无效方法
				for (Method iMethod : iClass.getMethods())
					if (!iMethod.isAnnotationPresent(Invalid.class) && compareMethod(iMethod, implMethod)) {
						isValidMethod = Boolean.TRUE;
						break;
					}
			if (!isValidMethod)
				continue;
			String sKey = getSKey(iClass, implMethod, false);
			if (StringUtil.isEmpty(sKey))
				continue;
			Service iss = (Service) IkasoaServerService.class.getDeclaredConstructors()[0].newInstance(implObject,
					implMethod,
					protocolHandlerFactory.getProtocolHandler(null, configurator.getProtocolHandlerClass()));
			log.debug("Builder Ikasoa service : {}", sKey);
			serviceMap.put(sKey, iss);
		}
	}

	private <T> BaseGetServiceFactory<Object[], T> getBaseGetServiceFactory() {
		BaseGetServiceFactory<Object[], T> bgsFactory = new BaseGetServiceFactory<>(thriftServerConfiguration,
				thriftClientConfiguration);
		bgsFactory.setProtocolHandlerClass(configurator.getProtocolHandlerClass());
		bgsFactory.setClientInvocationHandler(configurator.getClientInvocationHandler());
		return bgsFactory;
	}

	// 比较两个方法是否相同
	private boolean compareMethod(Method m1, Method m2) {
		if (m1 != null && m2 != null && m1.getName().equals(m2.getName())
				&& m1.getParameterTypes().length == m2.getParameterTypes().length
				&& m1.getReturnType().getName().equals(m2.getReturnType().getName())) {
			for (int i = 0; i < m1.getParameterTypes().length; i++)
				if (!m1.getParameterTypes()[i].getName().equals(m2.getParameterTypes()[i].getName()))
					return Boolean.FALSE;
			return Boolean.TRUE;
		} else
			return Boolean.FALSE;
	}

	private String getSKey(Class<?> iClass, Method method, boolean isCheckValid) {
		if (isCheckValid && (iClass == null || method == null || method.isAnnotationPresent(Invalid.class)))
			return null;
		return Optional.ofNullable(iClass.getAnnotation(IkasoaService.class))
				.map(s -> new ServiceKey(s.name(), method).toString())
				.orElse(new ServiceKey(iClass, method).toString());
	}

}
