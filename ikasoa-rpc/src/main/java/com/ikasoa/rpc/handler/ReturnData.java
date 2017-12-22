package com.ikasoa.rpc.handler;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ikasoa.core.utils.StringUtil;

/**
 * 返回数据对象
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
public class ReturnData {

	// 返回类型名称
	private String className;

	// 返回类型中的Type对象树组
	private Class<?>[] classTypes;

	// 异常类型树组
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
				if (type.getTypeName().indexOf("java.util.List") == 0) {
					classTypes[i] = List.class;
					setContainerType(Boolean.TRUE);
				} else if (type.getTypeName().indexOf("java.util.Set") == 0) {
					classTypes[i] = Set.class;
					setContainerType(Boolean.TRUE);
				} else if (type.getTypeName().indexOf("java.util.Map") == 0) {
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
		return List.class.getName().equals(className) || Set.class.getName().equals(className);
	}

	public boolean isMap() {
		return Map.class.getName().equals(className);
	}

	public boolean hasExcetion() {
		return getExcetionClassTypes().length > 0;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Class<?>[] getClassTypes() {
		return classTypes;
	}

	public Class<?> getClassType() {
		return getClassTypes(0);
	}

	public Class<?> getClassTypes(int k) {
		if (classTypes != null && classTypes.length > k) {
			return classTypes[k];
		} else {
			return Object.class;
		}
	}

	public void setClassTypes(Class<?>[] classTypes) {
		this.classTypes = classTypes;
	}

	public Class<?>[] getExcetionClassTypes() {
		return excetionClassTypes;
	}

	public void setExcetionClassTypes(Class<?>[] excetionClassTypes) {
		this.excetionClassTypes = excetionClassTypes;
	}

	public boolean isContainerType() {
		return isContainerType;
	}

	public void setContainerType(boolean isContainerType) {
		this.isContainerType = isContainerType;
	}

	private String getClassNameByTypeName(String ClassTypeName) {
		switch (ClassTypeName) {
		case "void":
			return null;
		case "byte":
			return Byte.class.getName();
		case "short":
			return Short.class.getName();
		case "int":
			return Integer.class.getName();
		case "long":
			return Long.class.getName();
		case "float":
			return Float.class.getName();
		case "double":
			return Double.class.getName();
		case "char":
			return Character.class.getName();
		case "boolean":
			return Boolean.class.getName();
		default:
			return ClassTypeName;
		}
	}

}