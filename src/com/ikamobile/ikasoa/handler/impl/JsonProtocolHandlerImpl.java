package com.ikamobile.ikasoa.handler.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sulei.core.utils.StringUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ikamobile.ikasoa.handler.ProtocolHandler;
import com.ikamobile.ikasoa.handler.ReturnData;

/**
 * 转换协议处理器JSON实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class JsonProtocolHandlerImpl<T1, T2> implements ProtocolHandler<T1, T2> {

	private Class<?>[] argClasses;

	private ReturnData resultData;

	private final static String E_KEY = "E__";

	public JsonProtocolHandlerImpl(Class<?>[] argClasses, ReturnData resultData) {
		this.argClasses = argClasses;
		this.resultData = resultData;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T1 strToArg(String str) {
		if (StringUtil.isEmpty(str)) {
			throw new RuntimeException("parameters string can't null !");
		}
		String[] strs = JSON.parseObject(str, String[].class);
		if (strs.length != argClasses.length) {
			throw new RuntimeException("parameters length is error !");
		}
		Object[] objs = new Object[argClasses.length];
		for (int i = 0; i < argClasses.length; i++) {
			String s = strs[i];
			Class<?> c = argClasses[i];
			if (isAppendQuotes(s)) {
				objs[i] = JSON.parseObject(new StringBuilder("\"").append(s).append("\"").toString(), c);
			} else {
				objs[i] = JSON.parseObject(s, c);
			}
		}
		return (T1) objs;
	}

	@Override
	public String argToStr(T1 arg) {
		return arg != null ? JSON.toJSONString(arg) : "[]";
	}

	@Override
	public String resultToStr(T2 result) {
		if (result instanceof Throwable) {
			return new StringBuilder(E_KEY).append(JSON.toJSONString(result)).toString();
		} else {
			return JSON.toJSONString(result);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public T2 strToResult(String str) {
		T2 result = null;
		if (resultData.isArray()) {
			if (List.class.getName().equals(resultData.getClassType().getName())
					|| Set.class.getName().equals(resultData.getClassType().getName())) {
				throw new RuntimeException("'List' or 'Set' must appoint type ! eg : 'List<String>' .");
			}
			result = (T2) JSON.parseArray(str, resultData.getClassType());
		} else if (resultData.isMap()) {
			if (Map.class.getName().equals(resultData.getClassType().getName())
					&& resultData.getClassTypes().length != 2) {
				throw new RuntimeException("'Map' must appoint type ! eg : 'Map<String, String>' .");
			}
			Map<Object, Object> map = new HashMap<Object, Object>();
			JSONObject jsonMap = JSON.parseObject(str);
			for (String key : jsonMap.keySet()) {
				Object valueObj = JSON.parseObject(jsonMap.get(key).toString(), resultData.getClassTypes(1));
				map.put(key, valueObj);
			}
			result = (T2) map;
		} else {
			result = (T2) JSON.parseObject(str, resultData.getClassType());
		}
		return result;
	}

	@Override
	public Throwable strToThrowable(String str) {
		String[] strs = str.split(E_KEY);
		if (strs.length == 2 && "".equals(strs[0])) {
			return JSON.parseObject(strs[1], Throwable.class);
		} else {
			return null;
		}
	}

	@Override
	public ReturnData getReturnData() {
		return resultData;
	}

	private boolean isAppendQuotes(String s) {
		return !((s.indexOf("[") == 0 && s.lastIndexOf("]") == s.length() - 1)
				|| (s.indexOf("{") == 0 && s.lastIndexOf("}") == s.length() - 1)
				|| (s.indexOf("\"") == 0 && s.lastIndexOf("\"") == s.length() - 1));
	}

}
