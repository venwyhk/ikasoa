package com.ikasoa.springboot.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.ikasoa.springboot.autoconfigure.ClientAutoConfiguration;

/**
 * RPC客户端注解
 * <p>
 * 该注解添加到客户端的SpringBootApplication.
 * <p>
 * 优先读取application.properties设置,其次读取该注解设置.
 * 
 * @author <a href="mailto:larry7696@gmail.com">Larry</a>
 * @version 0.1
 */
@Target(ElementType.TYPE)
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Import(ClientAutoConfiguration.class)
public @interface RpcClient {

	String host();

	int port();

	String conf() default "com.ikasoa.rpc.Configurator";

}