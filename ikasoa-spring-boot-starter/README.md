[![](https://raw.githubusercontent.com/venwyhk/ikasoa/master/ikasoalogo_small.png)](https://ikasoa.com)<br />

&nbsp;[![](https://img.shields.io/badge/license-MIT-097ABA.svg?style=plastic)](https://opensource.org/licenses/mit-license.php)&nbsp;&nbsp;

***

# ikasoa-spring-boot-starter #

## 概述 ##

  ikasoa的spring-boot启动器(starter),用于在spring-boot环境中使用ikasoa相关功能.

## 开发运行环境要求 ##

- 要求java运行环境为java8

## 环境搭建 ##

##### Maven配置 #####

pom.xml

```xml
    ......
    <dependency>
        <groupId>com.ikasoa</groupId>
        <artifactId>ikasoa-spring-boot-starter</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
    ......
```

  *需先下载源码并执行命令`mvn install`安装到本地maven库.*

## application.properties设置说明 ##

属性|描述|默认值|备注
-|-|-|-|-
spring.ikasoa.server.host|服务地址|localhost|
spring.ikasoa.server.port|服务端口|9999|
spring.ikasoa.server.service.implClasses|服务实现类地址|无|服务端设置时必填,多个实现类以","分隔.
spring.ikasoa.configuratorClass|设置类地址|com.ikasoa.rpc.Configurator|可继承Configurator实现高级设置

## 使用示例 ##

##### 服务端 #####

application.properties

```
    ......
    spring.ikasoa.server.service.implClasses=com.ikasoa.example.rpc.ExampleServiceImpl
    ......
```

  服务端必须在application.properties中设置spring.ikasoa.server.service.implClasses属性,该属性为允许远程调用的接口实现类,如有多个实现类以","分隔.

ServerStartupRunner.java

```java
    import org.springframework.stereotype.Component;
    import com.ikasoa.rpc.IkasoaException;
    import com.ikasoa.springboot.IkasoaServerRunner;

    @Component
    public class ServerStartupRunner extends IkasoaServerRunner {

        @Override
        protected void complete(String... args) throws IkasoaException {
            // 服务启动成功后执行
        }

        @Override
        protected void fail(String... args) throws IkasoaException {
            // 服务启动失败后执行
        }

    }
```

  设置ServerStartupRunner类后,ikasoa服务将会随spring-boot一起自动启动.

##### 客户端 #####

```java
    ......
    import javax.annotation.Resource;
    import com.ikasoa.example.rpc.ExampleService;
    ......
    @Resource
    ExampleService es;
    ......
    System.out.println(es.findVO(1).getString());
    ......
```

  如果调用远程服务,至少需在application.properties设置spring.ikasoa.server.host和spring.ikasoa.server.port属性,与服务端匹配.

## License ##

*ikasoa is free to use under [MIT license](https://github.com/venwyhk/ikasoa/blob/master/LICENSE).*

***

*larry7696@gmail.com*&nbsp;&nbsp;[![](https://i.creativecommons.org/l/by/4.0/80x15.png)](http://creativecommons.org/licenses/by/4.0/)