package com.ikasoa.core.nifty;

import org.apache.thrift.protocol.TProtocol;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class NiftyRequestContext implements RequestContext {
	private final ConnectionContext connectionContext;
	private final TProtocol inputProtocol;
	private final TProtocol outputProtocol;
	private final TNiftyTransport niftyTransport;
	private final ConcurrentMap<String, Object> data = new ConcurrentHashMap<>();

	@Override
	public TProtocol getInputProtocol() {
		return inputProtocol;
	}

	@Override
	public TProtocol getOutputProtocol() {
		return outputProtocol;
	}

	public TNiftyTransport getNiftyTransport() {
		return niftyTransport;
	}

	@Override
	public ConnectionContext getConnectionContext() {
		return connectionContext;
	}

	@Override
	public void setContextData(String key, Object val) {
		if (key == null)
			throw new NullPointerException(String.valueOf("context data key is null"));
		data.put(key, val);
	}

	@Override
	public Object getContextData(String key) {
		if (key == null)
			throw new NullPointerException(String.valueOf("context data key is null"));
		return data.get(key);
	}

	@Override
	public void clearContextData(String key) {
		if (key == null)
			throw new NullPointerException(String.valueOf("context data key is null"));
		data.remove(key);
	}

	@Override
	public Iterator<Map.Entry<String, Object>> contextDataIterator() {
		return Collections.unmodifiableSet(data.entrySet()).iterator();
	}

	public NiftyRequestContext(ConnectionContext connectionContext, TProtocol inputProtocol, TProtocol outputProtocol,
			TNiftyTransport niftyTransport) {
		this.connectionContext = connectionContext;
		this.niftyTransport = niftyTransport;
		this.inputProtocol = inputProtocol;
		this.outputProtocol = outputProtocol;
	}
}
