package com.ikasoa.core.netty;

import org.apache.thrift.protocol.TProtocol;

import java.util.Iterator;
import java.util.Map;

/**
 * RequestContext
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.6
 */
public interface RequestContext {

	TProtocol getOutputProtocol();

	TProtocol getInputProtocol();

	void setContextData(String key, Object val);

	Object getContextData(String key);

	void clearContextData(String key);

	Iterator<Map.Entry<String, Object>> contextDataIterator();

}
