package com.ikasoa.rpc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 无效方法注解
 * <p>
 * 可选配置,添加该注解的接口方法将不提供RPC服务.
 * <p>
 * 当接口的某些方法只允许本地调用,而不允许远程调用时,可使用该注解来标注,如果远程调用了含有该注解的接口方法,将会直接返回null.
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.3.2
 */
@Target(ElementType.METHOD)
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Invalid {
}
