package com.ikamobile.ikasoa.rpc.handler.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ikamobile.ikasoa.core.utils.StringUtil;
import com.ikamobile.ikasoa.rpc.handler.ProtocolHandler;
import com.ikamobile.ikasoa.rpc.handler.ReturnData;

/**
 * 转换协议处理器JSON实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class JsonProtocolHandlerImpl<T1, T2> implements ProtocolHandler<T1, T2> {

	private ReturnData resultData;

	// 类型分隔符
	private final static String CT = "&&&";

	// 异常标识符
	private final static String E = "E__";

	public JsonProtocolHandlerImpl(ReturnData resultData) {
		this.resultData = resultData;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T1 strToArg(String str) {
		if (StringUtil.isEmpty(str)) {
			throw new RuntimeException("parameters string can't null !");
		}
		if ("[]".equals(str)) {
			return null;
		}
		String[] strs = str.split(CT);
		if (strs.length != 2) {
			throw new RuntimeException("arg json string error : " + str);
		}
		String argClassStr = strs[0];
		Class<?>[] argClasses = JSON.parseObject(argClassStr, Class[].class);
		String argStr = strs[1];
		String[] argStrs = JSON.parseObject(argStr, String[].class);
		if (argStrs.length != argClasses.length) {
			throw new RuntimeException("parameters length is error !");
		}
		Object[] objs = new Object[argClasses.length];
		for (int i = 0; i < argClasses.length; i++) {
			String s = argStrs[i];
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
		if (arg == null) {
			return "[]";
		}
		Object[] args = (Object[]) arg;
		Class<?>[] argClasses = new Class<?>[args.length];
		for (int i = 0; i < args.length; i++) {
			argClasses[i] = args[i].getClass();
		}
		return new StringBuilder(JSON.toJSONString(argClasses)).append(CT).append(JSON.toJSONString(arg)).toString();
	}

	@Override
	public String resultToStr(T2 result) {
		if (result instanceof Throwable) {
			return new StringBuilder(E).append(JSON.toJSONString(result)).toString();
		} else {
			if (result != null) {
				return new StringBuilder(result.getClass().getName()).append(CT).append(JSON.toJSONString(result))
						.toString();
			} else {
				return null;
			}
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public T2 strToResult(String str) {
		if (str == null) {
			throw new RuntimeException("result string is null !");
		}
		String[] strs = str.split(CT);
		if (strs.length != 2) {
			throw new RuntimeException("result json string error : " + str);
		}
		String resultStr = strs[1];
		T2 result = null;
		if (resultData.isArray()) {
			if (List.class.getName().equals(resultData.getClassType().getName())
					|| Set.class.getName().equals(resultData.getClassType().getName())) {
				throw new RuntimeException("'List' or 'Set' must appoint type ! eg : 'List<String>' .");
			}
			result = (T2) JSON.parseArray(resultStr, resultData.getClassType());
		} else if (resultData.isMap()) {
			if (Map.class.getName().equals(resultData.getClassType().getName())
					&& resultData.getClassTypes().length != 2) {
				throw new RuntimeException("'Map' must appoint type ! eg : 'Map<String, String>' .");
			}
			Map<Object, Object> map = new HashMap<Object, Object>();
			JSONObject jsonMap = JSON.parseObject(resultStr);
			for (String key : jsonMap.keySet()) {
				Object valueObj = JSON.parseObject(jsonMap.get(key).toString(), resultData.getClassTypes(1));
				map.put(key, valueObj);
			}
			result = (T2) map;
		} else {
			try {
				// TODO: 暂时只有非集合类型才能识别真实数据类型
				result = (T2) JSON.parseObject(resultStr, Class.forName(strs[0]));
			} catch (Exception e) {
				result = (T2) JSON.parseObject("{}", resultData.getClassType());
			}
		}
		return result;
	}

	@Override
	public Throwable strToThrowable(String str) {
		String[] strs = str.split(E);
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
