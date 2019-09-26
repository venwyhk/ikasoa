package com.ikasoa.core.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.ikasoa.core.IkasoaException;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * Bean工具类
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@Slf4j
@UtilityClass
public class BeanUtil {

	/**
	 * 对象属性拷贝方法
	 * <p>
	 * 在Thrift对象与系统对象的转换可能会用到,所以这里提供了一个工具方法.
	 * <p>
	 * 如果对象结构比较复杂,更建议使用类似Dozer这样的工具进行映射转换.
	 * 
	 * @param target
	 *            输出的对象
	 * @param source
	 *            原始对象
	 * @exception IkasoaException
	 *                异常
	 */
	public static void copyProperties(Object target, Object source) throws IkasoaException {
		Class<?> sourceClz = source.getClass(), targetClz = target.getClass();
		Field[] fields = sourceClz.getDeclaredFields();
		if (fields.length == 0)
			fields = sourceClz.getSuperclass().getDeclaredFields();
		for (Field field : fields) {
			String fieldName = field.getName();
			Field targetField = null;
			try {
				targetField = targetClz.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				try {
					targetField = targetClz.getSuperclass().getDeclaredField(fieldName);
				} catch (NoSuchFieldException | SecurityException e1) {
					throw new IkasoaException(e1);
				}
			}
			if (ObjectUtil.same(field.getType(), targetField.getType())) {
				String getMethodName = StringUtil.merge("get", fieldName.substring(0, 1).toUpperCase(),
						fieldName.substring(1));
				String setMethodName = StringUtil.merge("set", fieldName.substring(0, 1).toUpperCase(),
						fieldName.substring(1));
				Method getMethod, setMethod;
				try {
					try {
						getMethod = sourceClz.getDeclaredMethod(getMethodName, new Class[] {});
					} catch (NoSuchMethodException e) {
						getMethod = sourceClz.getSuperclass().getDeclaredMethod(getMethodName, new Class[] {});
					}
					try {
						setMethod = targetClz.getDeclaredMethod(setMethodName, field.getType());
					} catch (NoSuchMethodException e) {
						setMethod = targetClz.getSuperclass().getDeclaredMethod(setMethodName, field.getType());
					}
					setMethod.invoke(target, getMethod.invoke(source, new Object[] {}));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new IkasoaException(e);
				} catch (NoSuchMethodException e) {
				} catch (Exception e) {
					log.warn("Object copy failed : {}", e.getMessage());
				}
			} else {
				throw new IkasoaException("Object copy failed ! Attribute type error !");
			}
		}
	}

}
