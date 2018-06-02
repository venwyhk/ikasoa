package com.ikasoa.core.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ikasoa.core.IkasoaException;

import lombok.extern.slf4j.Slf4j;

/**
 * Bean工具类
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@Slf4j
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
		Class<?> sourceClz = source.getClass();
		Class<?> targetClz = target.getClass();
		Field[] fields = sourceClz.getDeclaredFields();
		if (fields.length == 0)
			fields = sourceClz.getSuperclass().getDeclaredFields();
		for (short i = 0; i < fields.length; i++) {
			String fieldName = fields[i].getName();
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
			if (fields[i].getType() == targetField.getType()) {
				String getMethodName = new StringBuilder("get").append(fieldName.substring(0, 1).toUpperCase())
						.append(fieldName.substring(1)).toString();
				String setMethodName = new StringBuilder("set").append(fieldName.substring(0, 1).toUpperCase())
						.append(fieldName.substring(1)).toString();
				Method getMethod;
				Method setMethod;
				try {
					try {
						getMethod = sourceClz.getDeclaredMethod(getMethodName, new Class[] {});
					} catch (NoSuchMethodException e) {
						try {
							getMethod = sourceClz.getSuperclass().getDeclaredMethod(getMethodName, new Class[] {});
						} catch (NoSuchMethodException e1) {
							throw new IkasoaException(e1);
						}
					}
					try {
						setMethod = targetClz.getDeclaredMethod(setMethodName, fields[i].getType());
					} catch (NoSuchMethodException e) {
						try {
							setMethod = targetClz.getSuperclass().getDeclaredMethod(setMethodName, fields[i].getType());
						} catch (NoSuchMethodException e1) {
							throw new IkasoaException(e1);
						}
					}
					setMethod.invoke(target, getMethod.invoke(source, new Object[] {}));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new IkasoaException(e);
				} catch (Exception e) {
					log.warn("Object copy failed : {}", e.getMessage());
				}
			} else {
				throw new IkasoaException("Object copy failed ! Attribute type error !");
			}
		}
	}

}
