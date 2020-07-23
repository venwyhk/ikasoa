package com.ikasoa.rpc.handler;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ikasoa.core.utils.MapUtil;
import com.ikasoa.core.utils.ObjectUtil;
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

	private final static String[][] BASE_DATATYPES = { { "void", null }, { "byte", Byte.class.getName() },
			{ "short", Short.class.getName() }, { "int", Integer.class.getName() }, { "long", Long.class.getName() },
			{ "float", Float.class.getName() }, { "double", Double.class.getName() },
			{ "char", Character.class.getName() }, { "boolean", Boolean.class.getName() },
			{ "long", Long.class.getName() } };

	private final static Object[][] COLLECTION_DATATYPES = { { "java.util.List", List.class },
			{ "java.util.Set", Set.class }, { "java.util.Iterator", Iterator.class }, { "java.util.Map", Map.class },
			{ "java.util.Deque", Deque.class } };

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

	private boolean isContainerType = false;

	public ReturnData(Method method) {
		className = getClassNameByTypeName(method.getReturnType().getName());
		if (StringUtil.isEmpty(className))
			return;
		try {
			Type[] types = ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments();
			classTypes = new Class<?>[types.length];
			for (int i = 0; i < types.length; i++) {
				Type type = types[i];
				String typeName = StringUtil.isNotEmpty(type.getTypeName()) ? type.getTypeName().split("<")[0] : "";
				Map<Object, Object> dataTypeMap = MapUtil.arrayToMap(COLLECTION_DATATYPES);
				if (dataTypeMap.containsKey(typeName)) {
					classTypes[i] = (Class<?>) dataTypeMap.get(typeName);
					setContainerType(true);
				} else
					classTypes[i] = (Class<?>) type;
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
		return StringUtil.equals(List.class.getName(), getClassName())
				|| StringUtil.equals(Set.class.getName(), getClassName());
	}

	public boolean isMap() {
		return StringUtil.equals(Map.class.getName(), getClassName());
	}

	public boolean hasExcetion() {
		return ObjectUtil.isNotNull(getExcetionClassTypes()) && getExcetionClassTypes().length > 0;
	}

	public Class<?> getClassType() {
		return getClassTypes(0);
	}

	public Class<?> getClassTypes(int k) {
		return ObjectUtil.isNotNull(getClassTypes()) && getClassTypes().length > k ? getClassTypes()[k] : Object.class;
	}

	private String getClassNameByTypeName(String classTypeName) {
		Map<String, String> dataTypeMap = MapUtil.arrayToMap(BASE_DATATYPES);
		return (dataTypeMap.containsKey(classTypeName)) ? dataTypeMap.get(classTypeName) : classTypeName;
	}

}