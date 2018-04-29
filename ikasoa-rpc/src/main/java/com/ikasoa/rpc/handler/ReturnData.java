package com.ikasoa.rpc.handler;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ikasoa.core.utils.StringUtil;

import lombok.Data;

/**
 * 返回数据对象
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@Data
public class ReturnData {

	private final static String LIST_CLASS_NAME = "java.util.List";

	private final static String SET_CLASS_NAME = "java.util.Set";

	private final static String MAP_CLASS_NAME = "java.util.Map";

	private final static String[][] BASE_DATA_TYPES = { { "void", null }, { "byte", Byte.class.getName() },
			{ "short", Short.class.getName() }, { "int", Integer.class.getName() }, { "long", Long.class.getName() },
			{ "float", Float.class.getName() }, { "double", Double.class.getName() },
			{ "char", Character.class.getName() }, { "boolean", Boolean.class.getName() },
			{ "long", Long.class.getName() } };

	/**
	 * 返回类型名称
	 */
	private String className;

	/**
	 * 返回类型中的Type对象树组
	 */
	private Class<?>[] classTypes;

	/**
	 * 异常类型树组
	 */
	private Class<?>[] excetionClassTypes;

	private boolean isContainerType = Boolean.FALSE;

	public ReturnData(Method method) {
		className = getClassNameByTypeName(method.getReturnType().getName());
		if (StringUtil.isEmpty(className))
			return;
		try {
			Type[] types = ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments();
			classTypes = new Class<?>[types.length];
			for (int i = 0; i < types.length; i++) {
				Type type = types[i];
				if (type.getTypeName().indexOf(LIST_CLASS_NAME) == 0) {
					classTypes[i] = List.class;
					setContainerType(Boolean.TRUE);
				} else if (type.getTypeName().indexOf(SET_CLASS_NAME) == 0) {
					classTypes[i] = Set.class;
					setContainerType(Boolean.TRUE);
				} else if (type.getTypeName().indexOf(MAP_CLASS_NAME) == 0) {
					classTypes[i] = Map.class;
					setContainerType(Boolean.TRUE);
				} else {
					classTypes[i] = (Class<?>) types[i];
				}
			}
		} catch (Exception e) {
			try {
				this.classTypes = new Class<?>[] { Class.forName(this.className) };
			} catch (ClassNotFoundException ex) {
				throw new RuntimeException("Create return data object exception !", ex);
			}
		}
		this.setExcetionClassTypes(method.getExceptionTypes());
	}

	public ReturnData(Class<?> clazz) {
		className = clazz.getName();
		classTypes = new Class<?>[] { clazz };
	}

	public ReturnData(String className, Class<?> clazz) {
		this.className = className;
		classTypes = new Class<?>[] { clazz };
	}

	public ReturnData(String className, Class<?>[] classes) {
		this.className = className;
		this.classTypes = classes;
	}

	public boolean isArray() {
		return List.class.getName().equals(getClassName()) || Set.class.getName().equals(getClassName());
	}

	public boolean isMap() {
		return Map.class.getName().equals(getClassName());
	}

	public boolean hasExcetion() {
		return getExcetionClassTypes() != null && getExcetionClassTypes().length > 0;
	}

	public Class<?> getClassType() {
		return getClassTypes(0);
	}

	public Class<?> getClassTypes(int k) {
		return getClassTypes() != null && getClassTypes().length > k ? getClassTypes()[k] : Object.class;
	}

	private String getClassNameByTypeName(String classTypeName) {
		Map<String, String> dataTypeMap = toMap(BASE_DATA_TYPES);
		return (dataTypeMap.containsKey(classTypeName)) ? dataTypeMap.get(classTypeName) : classTypeName;
	}

	public Map<String, String> toMap(String[][] array) {
		final Map<String, String> map = new HashMap<>((int) (array.length * 1.5));
		for (int i = 0; i < array.length; i++) {
			final String[] entry = array[i];
			if (entry.length < 2)
				throw new IllegalArgumentException(
						"Array element " + i + ", '" + entry + "', has a length less than 2");
			map.put(entry[0], entry[1]);
		}
		return map;
	}

}