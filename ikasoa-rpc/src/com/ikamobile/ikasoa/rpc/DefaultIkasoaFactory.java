package com.ikamobile.ikasoa.rpc;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ikamobile.ikasoa.core.STException;
import com.ikamobile.ikasoa.core.loadbalance.LoadBalance;
import com.ikamobile.ikasoa.core.loadbalance.ServerInfo;
import com.ikamobile.ikasoa.core.thrift.GeneralFactory;
import com.ikamobile.ikasoa.core.thrift.client.ThriftClient;
import com.ikamobile.ikasoa.core.thrift.client.ThriftClientConfiguration;
import com.ikamobile.ikasoa.core.thrift.server.ThriftServer;
import com.ikamobile.ikasoa.core.thrift.server.ThriftServerConfiguration;
import com.ikamobile.ikasoa.core.thrift.service.Service;
import com.ikamobile.ikasoa.rpc.handler.ProtocolHandlerFactory;
import com.ikamobile.ikasoa.rpc.handler.ReturnData;
import com.ikamobile.ikasoa.rpc.service.IkasoaServerService;
import com.ikamobile.ikasoa.rpc.service.impl.IkasoaServerImpl;

/**
 * 默认IKASOA服务工厂
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class DefaultIkasoaFactory extends GeneralFactory implements IkasoaFactory {

	private static final Logger LOG = LoggerFactory.getLogger(DefaultIkasoaFactory.class);

	private Configurator configurator = new Configurator();

	public DefaultIkasoaFactory() {
	}

	public DefaultIkasoaFactory(ThriftServerConfiguration thriftServerConfiguration) {
		super.thriftServerConfiguration = thriftServerConfiguration;
		this.configurator.setThriftServerConfiguration(thriftServerConfiguration);
	}

	public DefaultIkasoaFactory(ThriftClientConfiguration thriftClientConfiguration) {
		super.thriftClientConfiguration = thriftClientConfiguration;
		this.configurator.setThriftClientConfiguration(thriftClientConfiguration);
	}

	public DefaultIkasoaFactory(Configurator configurator) {
		if (configurator != null) {
			this.configurator = configurator;
			if (configurator.getThriftServerConfiguration() != null) {
				super.thriftServerConfiguration = configurator.getThriftServerConfiguration();
			}
			if (configurator.getThriftClientConfiguration() != null) {
				super.thriftClientConfiguration = configurator.getThriftClientConfiguration();
			}
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getIkasoaClient(Class<T> iClass, String serverHost, int serverPort) {
		BaseGetServiceFactory<Object[], T> bgsFactory = new BaseGetServiceFactory<Object[], T>(
				super.thriftServerConfiguration, super.thriftClientConfiguration);
		bgsFactory.setProtocolHandlerClass(configurator.getProtocolHandlerClass());
		bgsFactory.setClientInvocationHandler(configurator.getClientInvocationHandler());
		return (T) Proxy.newProxyInstance(iClass.getClassLoader(), new Class<?>[] { iClass },
				(proxy, iMethod, args) -> bgsFactory
						.getBaseGetService(serverHost, serverPort, getSKey(iClass, iMethod), new ReturnData(iMethod))
						.get(args));
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getIkasoaClient(Class<T> iClass, List<ServerInfo> serverInfoList) {
		BaseGetServiceFactory<Object[], T> bgsFactory = new BaseGetServiceFactory<Object[], T>(
				super.thriftServerConfiguration, super.thriftClientConfiguration);
		bgsFactory.setProtocolHandlerClass(configurator.getProtocolHandlerClass());
		bgsFactory.setClientInvocationHandler(configurator.getClientInvocationHandler());
		return (T) Proxy.newProxyInstance(iClass.getClassLoader(), new Class<?>[] { iClass },
				(proxy, iMethod, args) -> bgsFactory
						.getBaseGetService(serverInfoList, getSKey(iClass, iMethod), new ReturnData(iMethod))
						.get(args));
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getIkasoaClient(Class<T> iClass, List<ServerInfo> serverInfoList,
			Class<LoadBalance> loadBalanceClass) {
		BaseGetServiceFactory<Object[], T> bgsFactory = new BaseGetServiceFactory<Object[], T>(
				super.thriftServerConfiguration, super.thriftClientConfiguration);
		bgsFactory.setProtocolHandlerClass(configurator.getProtocolHandlerClass());
		bgsFactory.setClientInvocationHandler(configurator.getClientInvocationHandler());
		return (T) Proxy.newProxyInstance(iClass.getClassLoader(), new Class<?>[] { iClass },
				(proxy, iMethod, args) -> bgsFactory.getBaseGetService(serverInfoList, loadBalanceClass,
						getSKey(iClass, iMethod), new ReturnData(iMethod)).get(args));
	}

	@Override
	public IkasoaServer getIkasoaServer(Class<?> implClass, int serverPort) throws IkasoaException {
		return getIkasoaServer(serverPort, getServiceMapByImplClass(new ImplClsCon(implClass)));
	}

	@Override
	public IkasoaServer getIkasoaServer(ImplClsCon implClsCon, int serverPort) throws IkasoaException {
		return getIkasoaServer(serverPort, getServiceMapByImplClass(implClsCon));
	}

	@Override
	public IkasoaServer getIkasoaServer(List<ImplClsCon> implClsConList, int serverPort) throws IkasoaException {
		Map<String, Service> serviceMap = new HashMap<>();
		for (ImplClsCon implClsCon : implClsConList) {
			serviceMap.putAll(getServiceMapByImplClass(implClsCon));
		}
		return getIkasoaServer(serverPort, serviceMap);
	}

	@Override
	public IkasoaServer getIkasoaServer(int serverPort, Map<String, Service> serviceMap) throws IkasoaException {
		try {
			return new IkasoaServerImpl(super.getThriftServer(serverPort, serviceMap), serviceMap);
		} catch (STException e) {
			throw new IkasoaException("Create Ikasoa service exception !", e);
		}
	}

	@Override
	public Service getService(ThriftClient thriftClient) throws STException {
		return super.getService(thriftClient);
	}

	@Override
	public Service getService(ThriftClient thriftClient, String serviceName) throws STException {
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
	public ThriftServer getThriftServer(int serverPort, Map<String, Service> serviceMap) throws STException {
		return super.getThriftServer(serverPort, serviceMap);
	}

	@Override
	public ThriftServer getThriftServer(String serverName, int serverPort, Service service) {
		return super.getThriftServer(serverName, serverPort, service);
	}

	@Override
	public ThriftServer getThriftServer(String serverName, int serverPort, Map<String, Service> serviceMap)
			throws STException {
		return super.getThriftServer(serverName, serverPort, serviceMap);
	}

	private Map<String, Service> getServiceMapByImplClass(ImplClsCon implClsCon) throws IkasoaException {
		return getServiceMapByClass(new HashMap<String, Service>(), implClsCon.getImplClass(),
				implClsCon.getImplObject(), implClsCon.getImplClass());
	}

	private Map<String, Service> getServiceMapByClass(Map<String, Service> serviceMap, Class<?> implClass,
			Object implObject, Class<?> superClass) throws IkasoaException {
		if (implClass == null) {
			throw new IkasoaException("Implement class is not null !");
		}
		if (implClass.getInterfaces().length == 0) {
			LOG.warn("Class (" + implClass.getName() + ") is not this interface implement class , Will ignore .");
		}
		for (Class<?> iClass : superClass.getInterfaces()) {
			buildService(serviceMap, iClass, implClass, implObject);
		}
		if (superClass.getSuperclass() != null && !Object.class.equals(superClass.getSuperclass())) {
			serviceMap.putAll(getServiceMapByClass(serviceMap, implClass, implObject, superClass.getSuperclass()));
		}
		return serviceMap;
	}

	private void buildService(Map<String, Service> serviceMap, Class<?> iClass, Class<?> implClass, Object implObject)
			throws IkasoaException {
		try {
			if (implObject == null) {
				implObject = implClass.newInstance();
			}
			ProtocolHandlerFactory<Object[], Object> protocolHandlerFactory = new ProtocolHandlerFactory<Object[], Object>();
			for (Method implMethod : implClass.getMethods()) {

				boolean isValidMethod = false;
				// 对hashCode和toString两个方法做特殊处理
				if ("hashCode".equals(implMethod.getName()) || "toString".equals(implMethod.getName())) {
					isValidMethod = true;
				} else {
					// 过滤掉无效方法
					for (Method iMethod : iClass.getMethods()) {
						if (compareMethod(iMethod, implMethod)) {
							isValidMethod = true;
							break;
						}
					}
				}
				if (!isValidMethod) {
					continue;
				}
				String sKey = getSKey(iClass, implMethod);
				Service iss = (Service) IkasoaServerService.class.getDeclaredConstructors()[0].newInstance(implObject,
						implMethod,
						protocolHandlerFactory.getProtocolHandler(null, configurator.getProtocolHandlerClass()));
				LOG.debug("Builder Ikasoa service : " + sKey);
				serviceMap.put(sKey, iss);
			}
		} catch (Exception e) {
			throw new IkasoaException("Builder Ikasoa service exception !", e);
		}
	}

	// 比较两个方法是否相同
	private boolean compareMethod(Method m1, Method m2) {
		if (m1 != null && m2 != null && m1.getName().equals(m2.getName())
				&& m1.getParameterTypes().length == m2.getParameterTypes().length
				&& m1.getReturnType().getName().equals(m2.getReturnType().getName())) {
			for (int i = 0; i < m1.getParameterTypes().length; i++) {
				if (!m1.getParameterTypes()[i].getName().equals(m2.getParameterTypes()[i].getName())) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	private String getSKey(Class<?> iClass, Method method) {
		return new ServiceKey(iClass, method).toString();
	}

}
