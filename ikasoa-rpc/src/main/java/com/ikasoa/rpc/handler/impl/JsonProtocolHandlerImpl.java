package com.ikasoa.rpc.handler.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ikasoa.core.utils.StringUtil;
import com.ikasoa.rpc.handler.ProtocolHandler;
import com.ikasoa.rpc.handler.ReturnData;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 转换协议处理器JSON实现
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@AllArgsConstructor
@NoArgsConstructor
public class JsonProtocolHandlerImpl<T, R> implements ProtocolHandler<T, R> {

	private ReturnData resultData;

	@Override
	@SuppressWarnings("unchecked")
	public T strToArg(String str) {
		if (StringUtil.isEmpty(str))
			throw new IllegalArgumentException("parameters string can't null !");
		if ("[]".equals(str))
			return null;
		String[] strs = str.split(String.valueOf(CT));
		if (strs.length != 2)
			throw new IllegalArgumentException("arg json string error : " + str);
		String argClassStr = strs[0];
		Class<?>[] argClasses = JSON.parseObject(argClassStr, Class[].class);
		String argStr = strs[1];
		String[] argStrs = JSON.parseObject(argStr, String[].class);
		if (argStrs.length != argClasses.length)
			throw new IllegalArgumentException("parameters length is error !");
		Object[] objs = new Object[argClasses.length];
		for (int i = 0; i < argClasses.length; i++) {
			String s = argStrs[i];
			Class<?> c = argClasses[i];
			if (StringUtil.isEmpty(s) || c == null) {
				objs[i] = null;
				continue;
			}
			objs[i] = isAppendQuotes(s) ? JSON.parseObject(new StringBuilder("\"").append(s).append("\"").toString(), c)
					: JSON.parseObject(s, c);
		}
		return (T) objs;
	}

	@Override
	public String argToStr(T arg) {
		if (arg == null)
			return "[]";
		Object[] args = (Object[]) arg;
		Class<?>[] argClasses = new Class<?>[args.length];
		for (int i = 0; i < args.length; i++)
			if (args[i] != null)
				argClasses[i] = args[i].getClass();
			else
				continue;
		return new StringBuilder(JSON.toJSONString(argClasses)).append(CT).append(JSON.toJSONString(arg)).toString();
	}

	@Override
	public String resultToStr(R result) {
		return result instanceof Throwable ? new StringBuilder(E).append(JSON.toJSONString(result)).toString()
				: Optional
						.ofNullable(result).map(r -> new StringBuilder(r.getClass().getName())
								.append(String.valueOf(CT)).append(JSON.toJSONString(r)).toString())
						.orElse(String.valueOf(V));
	}

	@Override
	@SuppressWarnings("unchecked")
	public R strToResult(String str) {
		if (str == null)
			throw new IllegalArgumentException("result string is null !");
		if (String.valueOf(ProtocolHandler.V).equals(str))
			return null;
		String[] strs = str.split(String.valueOf(CT));
		if (strs.length != 2)
			throw new IllegalArgumentException("result json string error : " + str);
		String resultStr = strs[1];
		R result = null;
		if (resultData.isArray()) {
			if (!resultData.isContainerType() && (List.class.getName().equals(resultData.getClassType().getName())
					|| Set.class.getName().equals(resultData.getClassType().getName())))
				throw new IllegalArgumentException("'List' or 'Set' must appoint type ! eg : 'List<String>' .");
			result = (R) JSON.parseArray(resultStr, resultData.getClassType());
		} else if (resultData.isMap()) {
			if (!resultData.isContainerType() && (Map.class.getName().equals(resultData.getClassType().getName())
					&& resultData.getClassTypes().length != 2))
				throw new IllegalArgumentException("'Map' must appoint type ! eg : 'Map<String, String>' .");
			JSONObject jsonMap = JSON.parseObject(resultStr);
			Map<Object, Object> map = new HashMap<>(jsonMap.size());
			for (String key : jsonMap.keySet())
				map.put(key, JSON.parseObject(jsonMap.get(key).toString(), resultData.getClassTypes(1)));
			result = (R) map;
		} else {
			try {
				// TODO: 暂时只有非集合类型才能识别真实数据类型
				result = (R) JSON.parseObject(resultStr, Class.forName(strs[0]));
			} catch (Exception e) {
				result = (R) JSON.parseObject("{}", resultData.getClassType());
			}
		}
		return result;
	}

	@Override
	public Throwable strToThrowable(String str) {
		String[] strs = str.split(String.valueOf(E));
		return strs.length == 2 && "".equals(strs[0]) ? JSON.parseObject(strs[1], Throwable.class) : null;
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
