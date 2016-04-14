package com.ikamobile.ikasoa;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sulei.core.STException;
import org.sulei.core.thrift.GeneralFactory;
import org.sulei.core.thrift.client.ThriftClient;
import org.sulei.core.thrift.client.ThriftClientConfiguration;
import org.sulei.core.thrift.server.ThriftServer;
import org.sulei.core.thrift.server.ThriftServerConfiguration;
import org.sulei.core.thrift.service.Service;

import com.ikamobile.ikasoa.handler.ProtocolHandlerFactory;
import com.ikamobile.ikasoa.handler.ProtocolHandlerFactory.ProtocolType;
import com.ikamobile.ikasoa.handler.ReturnData;
import com.ikamobile.ikasoa.service.IkasoaServerService;
import com.ikamobile.ikasoa.service.impl.IkasoaServerImpl;

/**
 * 默认IKASOA服务工厂
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class DefaultIkasoaFactory extends GeneralFactory implements IkasoaFactory {

	private static final Logger LOG = LoggerFactory.getLogger(DefaultIkasoaFactory.class);

	protected ProtocolType protocolType;

	public DefaultIkasoaFactory() {
	}

	public DefaultIkasoaFactory(ProtocolType protocolType) {
		this.protocolType = protocolType;
	}

	public DefaultIkasoaFactory(ThriftServerConfiguration thriftServerConfiguration) {
		super.thriftServerConfiguration = thriftServerConfiguration;
	}

	public DefaultIkasoaFactory(ThriftClientConfiguration thriftClientConfiguration) {
		this.thriftClientConfiguration = thriftClientConfiguration;
	}

	public DefaultIkasoaFactory(ThriftServerConfiguration thriftServerConfiguration,
			ThriftClientConfiguration thriftClientConfiguration) {
		super.thriftServerConfiguration = thriftServerConfiguration;
		super.thriftClientConfiguration = thriftClientConfiguration;
	}

	public DefaultIkasoaFactory(ProtocolType protocolType, ThriftServerConfiguration thriftServerConfiguration,
			ThriftClientConfiguration thriftClientConfiguration) {
		this.protocolType = protocolType;
		super.thriftServerConfiguration = thriftServerConfiguration;
		super.thriftClientConfiguration = thriftClientConfiguration;
	}

	// 获取一个客户端接口实现
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getIkasoaClient(final Class<T> iClass, final String serverHost, final int serverPort) {
		final BaseGetServiceFactory<Object[], T> bgsFactory = new BaseGetServiceFactory<Object[], T>(
				super.thriftServerConfiguration, super.thriftClientConfiguration);
		bgsFactory.setProtocolType(protocolType);
		return (T) Proxy.newProxyInstance(iClass.getClassLoader(), new Class<?>[] { iClass }, new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method iMethod, Object[] args) throws Throwable {
				String sKey = getSKey(iClass, iMethod);
				LOG.debug("server key : " + sKey);
				BaseGetService<Object[], T> s = bgsFactory.getBaseGetService(serverHost, serverPort, sKey,
						new ReturnData(iMethod));
				return s.get(args);
			}
		});
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getIkasoaClient(final Class<T> iClass, final List<String> serverHostList, final int serverPort) {
		final BaseGetServiceFactory<Object[], T> bgsFactory = new BaseGetServiceFactory<Object[], T>(
				super.thriftServerConfiguration, super.thriftClientConfiguration);
		bgsFactory.setProtocolType(protocolType);
		return (T) Proxy.newProxyInstance(iClass.getClassLoader(), new Class<?>[] { iClass }, new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method iMethod, Object[] args) throws Throwable {
				String sKey = getSKey(iClass, iMethod);
				LOG.debug("server key : " + sKey);
				BaseGetService<Object[], T> s = bgsFactory.getBaseGetService(serverHostList, serverPort, sKey,
						new ReturnData(iMethod));
				return s.get(args);
			}
		});
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
		Map<String, Service> serviceMap = new HashMap<String, Service>();
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
	public Service getService(ThriftClient arg0) throws STException {
		return super.getService(arg0);
	}

	@Override
	public Service getService(ThriftClient arg0, String arg1) throws STException {
		return super.getService(arg0, arg1);
	}

	@Override
	public ThriftClient getThriftClient(String arg0, int arg1) {
		return super.getThriftClient(arg0, arg1);
	}

	@Override
	public ThriftClient getThriftClient(List<String> arg0, int arg1) {
		return super.getThriftClient(arg0, arg1);
	}

	@Override
	public ThriftServer getThriftServer(int arg0, Service arg1) {
		return super.getThriftServer(arg0, arg1);
	}

	@Override
	public ThriftServer getThriftServer(int arg0, Map<String, Service> arg1) throws STException {
		return super.getThriftServer(arg0, arg1);
	}

	@Override
	public ThriftServer getThriftServer(String arg0, int arg1, Service arg2) {
		return super.getThriftServer(arg0, arg1, arg2);
	}

	@Override
	public ThriftServer getThriftServer(String arg0, int arg1, Map<String, Service> arg2) throws STException {
		return super.getThriftServer(arg0, arg1, arg2);
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
				// 过滤掉无效方法
				boolean isValidMethod = false;
				for (Method iMethod : iClass.getMethods()) {
					if (compareMethod(iMethod, implMethod)) {
						isValidMethod = true;
						break;
					}
				}
				if (!isValidMethod) {
					continue;
				}
				String sKey = getSKey(iClass, implMethod);
				Service iss = (Service) IkasoaServerService.class.getDeclaredConstructors()[0].newInstance(implObject,
						implMethod,
						protocolHandlerFactory.getProtocolHandler(implMethod.getParameterTypes(), null, protocolType));
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
				&& m1.getParameterTypes().length == m2.getParameterTypes().length) {
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
