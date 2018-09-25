package com.ikasoa.core.nifty;

import org.apache.thrift.protocol.TProtocol;

import java.util.Iterator;
import java.util.Map;

public interface RequestContext {
	TProtocol getOutputProtocol();

	TProtocol getInputProtocol();

	ConnectionContext getConnectionContext();

	void setContextData(String key, Object val);

	Object getContextData(String key);

	void clearContextData(String key);

	Iterator<Map.Entry<String, Object>> contextDataIterator();
}
