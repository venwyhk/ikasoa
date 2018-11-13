[![](http://ikasoa.com/ikasoalogo_small.png)](http://ikasoa.com)<br />

&nbsp;[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.ikasoa/ikasoa-spring-boot-starter/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.ikasoa/ikasoa-spring-boot-starter)&nbsp;&nbsp;[![Javadocs](http://javadoc.io/badge/com.ikasoa/ikasoa-spring-boot-starter.svg)](http://javadoc.io/doc/com.ikasoa/ikasoa-spring-boot-starter)&nbsp;&nbsp;

***

# ikasoa spring-boot-starter #

## 概述 ##

  ikasoa的spring-boot启动器(starter),用于在spring-boot项目中使用ikasoa相关功能.

## 开发运行环境要求 ##

- 要求java运行环境为java8

- 支持spring-boot版本1.5.*

## 环境搭建 ##

##### 引用依赖包 #####

Maven

```xml
    ......
    <dependency>
        <groupId>com.ikasoa</groupId>
        <artifactId>ikasoa-spring-boot-starter</artifactId>
        <version>0.1.2-BETA</version>
    </dependency>
    ......
```

Gradle

```
    compile group: 'com.ikasoa', name: 'ikasoa-spring-boot-starter', version: '0.1.2-BETA'
```

## application.properties设置说明 ##

<table>
  <tr>
    <td>属性</td>
    <td>描述</td>
    <td>默认值</td>
    <td>备注</td>
  </tr>
  <tr>
    <td>ikasoa.server.host</td>
    <td>服务地址</td>
    <td>localhost</td>
    <td></td>
  </tr>
  <tr>
    <td>ikasoa.server.port</td>
    <td>服务端口</td>
    <td>9999</td>
    <td>需为1024到65535之间的数值,且不能使用已被占用的端口号.</td>
  </tr>
  <tr>
    <td>ikasoa.server.names</td>
    <td>服务名称</td>
    <td>(无)</td>
    <td>服务端设置时与'ikasoa.server.classes'必填其中一项,可由@Service注解定义,多项可以","分隔.客户端设置不需要填写.(推荐)</td>
  </tr>
  <tr>
    <td>ikasoa.server.classes</td>
    <td>服务实现类名</td>
    <td>(无)</td>
    <td>服务端设置时与'ikasoa.server.names'必填其中一项,设置服务接口实现类的完整类路径,多项可以","分隔.客户端设置不需要填写.</td>
  </tr>
  <tr>
    <td>ikasoa.configurator</td>
    <td>服务配置类地址</td>
    <td>com.ikasoa.rpc.Configurator</td>
    <td>可继承Configurator实现高级设置.</td>
  </tr>
  <tr>
    <td>ikasoa.server.zkserver</td>
    <td>zookeeper服务注册地址</td>
    <td>(无)</td>
    <td>可选配置,zookeeper服务器信息.例如:'localhost:2181'.如有多条服务器信息则以','分隔.</td>
  </tr>
  <tr>
    <td>ikasoa.server.zknode</td>
    <td>zookeeper节点路径</td>
    <td>/</td>
    <td>可选配置,zookeeper节点,如果不设置默认为根节点.</td>
  </tr>
</table>

## 使用示例 ##

##### 服务端 #####

application.properties

```
    ......
    ikasoa.server.names=exampleService
    # ikasoa.server.port=9999
    # ikasoa.server.classes=com.ikasoa.example.rpc.ExampleServiceImpl
    ......
```

  *服务端须在application.properties中设置'ikasoa.server.names'(或'ikasoa.server.classes')属性,该属性为允许远程调用的接口实现类名称(或类路径).如有多个可以","分隔.*
  
  *其中类名对应注解:@Service("exampleService")中定义的名称.*
  
ServerStartupRunner.java

```java
    import org.springframework.stereotype.Component;
    import com.ikasoa.springboot.runner.IkasoaServerRunner;

    @Component
    public class ServerStartupRunner extends IkasoaServerRunner {
    }
```

  *创建ServerStartupRunner类,ikasoa服务将会随spring-boot一起自动启动.*

##### 客户端 #####

配置方式(单服务)

application.properties

```
    ......
    ikasoa.server.host=xxx.xxx.xxx.xxx
    ikasoa.server.port=9999
    ......
```

```java
    ......
    import org.springframework.beans.factory.annotation.Autowired;
    import com.ikasoa.springboot.ServiceProxy;
    import com.ikasoa.example.rpc.ExampleService;
    ......
    @Autowired
    ServiceProxy proxy;
    ......
    ExampleService es = proxy.getService(ExampleService.class);
    System.out.println(es.findVO(1).getString());
    ......
```

  *在application.properties设置'ikasoa.server.host'和'ikasoa.server.port'属性,并与服务端匹配.*
  
注解方式(单服务)

Application.java

```java
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    import com.ikasoa.springboot.annotation.RpcClient;

    @SpringBootApplication
    @RpcClient(host = "xxx.xxx.xxx.xxx", port = 9999)
    public class Application {
        public static void main(String[] args) throws Exception {
            SpringApplication.run(Application.class, args);
        }
    }
```

```java
    ......
    import org.springframework.beans.factory.annotation.Autowired;
    import com.ikasoa.springboot.ServiceProxy;
    import com.ikasoa.example.rpc.ExampleService;
    ......
    @Autowired
    ServiceProxy proxy;
    ......
    ExampleService es = proxy.getService(ExampleService.class);
    System.out.println(es.findVO(1).getString());
    ......
```

编码方式(多服务)

```java
    ......
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.cloud.client.ServiceInstance;
    import org.springframework.cloud.client.discovery.DiscoveryClient;
    import com.ikasoa.springboot.ServiceProxy;
    import com.ikasoa.example.rpc.ExampleService;
    ......
    ServiceProxy proxy = new ServiceProxy("xxx.xxx.xxx.xxx", 9999);
    // ServiceProxy proxy2 = new ServiceProxy("xxx.xxx.xxx.xxx", 9998);
    ......
    ExampleService es = proxy.getService(ExampleService.class);
    System.out.println(es.findVO(1).getString());
    ......
```
  
## 与Eureka结合 ##

  ikasoaServer可以直接注册到Eureka,与其它springboot项目并无区别.

##### 引用Eureka依赖包 #####

Maven

```xml
    ......
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-eureka</artifactId>
        <version>1.4.4.RELEASE</version>
    </dependency>
    ......
```

Gradle

```
    compile 'org.springframework.cloud:spring-cloud-starter-eureka:1.4.4.RELEASE'
```

##### 设置EurekaServer地址 #####

application.properties

```
    eureka.client.serviceUrl.defaultZone=http://[EurekaServerHost]:[EurekaServerPort]/eureka/v2/
```

##### Application增加@EnableEurekaClient注解 #####

Application.java

```java
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

    @EnableEurekaClient
    @SpringBootApplication
    public class Application {
        public static void main(String[] args) throws Exception {
            SpringApplication.run(Application.class, args);
        }
    }
```

##### 客户端从Eureka获取IkasoaServer地址 #####

application.properties

```
    eureka.client.serviceUrl.defaultZone=http://[EurekaServerHost]:[EurekaServerPort]/eureka/v2/
```

注解方式(单服务)

Application.java

```java
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    import com.ikasoa.springboot.annotation.RpcEurekaClient;

    @SpringBootApplication
    @RpcEurekaClient(name = "[服务端注册到Eureka的名称]", port = 9999)
    public class Application {
        public static void main(String[] args) throws Exception {
            SpringApplication.run(Application.class, args);
        }
    }
```

```java
    ......
    import org.springframework.beans.factory.annotation.Autowired;
    import com.ikasoa.springboot.ServiceProxy;
    import com.ikasoa.example.rpc.ExampleService;
    ......
    @Autowired
    ServiceProxy proxy;
    ......
    ExampleService es = proxy.getService(ExampleService.class);
    System.out.println(es.findVO(1).getString());
    ......
```

编码方式(多服务)

```java
    ......
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.cloud.client.ServiceInstance;
    import org.springframework.cloud.client.discovery.DiscoveryClient;
    import com.ikasoa.springboot.ServiceProxy;
    import com.ikasoa.example.rpc.ExampleService;
    ......
    @Autowired
    DiscoveryClient discoveryClient;
    int port = 9999; // 服务端口
    ......
    ServiceInstance instance = discoveryClient.getInstances("[服务端注册到Eureka的名称]").get(0);
    ServiceProxy proxy = new ServiceProxy(instance.getHost(), port);
    ......
    ExampleService es = proxy.getService(ExampleService.class);
    System.out.println(es.findVO(1).getString());
    ......
```

## License ##

*ikasoa is free to use under [MIT license](https://github.com/venwyhk/ikasoa/blob/master/LICENSE).*

***

*larry7696@gmail.com*&nbsp;&nbsp;[![](https://i.creativecommons.org/l/by/4.0/80x15.png)](http://creativecommons.org/licenses/by/4.0/)
