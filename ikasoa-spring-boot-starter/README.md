[![](https://raw.githubusercontent.com/venwyhk/ikasoa/master/ikasoalogo_small.png)](http://ikasoa.com)<br />

&nbsp;[![](https://codeship.com/projects/9cf2f150-1507-0134-ee57-3adebfc67210/status?branch=master)](https://codeship.com/projects/157977)&nbsp;&nbsp;[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.ikasoa/ikasoa-spring-boot-starter/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/com.ikasoa/ikasoa-spring-boot-starter)&nbsp;&nbsp;[![Javadocs](http://javadoc.io/badge/com.ikasoa/ikasoa-spring-boot-starter.svg?style=plastic)](http://javadoc.io/doc/com.ikasoa/ikasoa-spring-boot-starter)&nbsp;&nbsp;[![](https://img.shields.io/badge/license-MIT-097ABA.svg?style=plastic)](https://opensource.org/licenses/mit-license.php)&nbsp;&nbsp;

***

# ikasoa spring-boot-starter #

## 概述 ##

  ikasoa的spring-boot启动器(starter),用于在spring-boot项目中使用ikasoa相关功能.

## 开发运行环境要求 ##

- 要求java运行环境为java8

## 环境搭建 ##

##### Maven配置 #####

需要修改pom.xml文件,添加ikasoa-spring-boot-starter的依赖:

pom.xml

```xml
    ......
    <dependency>
        <groupId>com.ikasoa</groupId>
        <artifactId>ikasoa-spring-boot-starter</artifactId>
        <version>0.1-ALPHA</version>
    </dependency>
    ......
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
    <td>服务端设置时与'ikasoa.server.classes'必填其中一项,可由@Service注解定义,多项可以","分隔.客户端设置不需要填写.</td>
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
</table>

## 使用示例 ##

##### 服务端 #####

application.properties

```
    ......
    ikasoa.server.names=exampleService
    #ikasoa.server.classes=com.ikasoa.example.rpc.ExampleServiceImpl
    ......
```

  服务端须在application.properties中设置'ikasoa.server.names'(或'ikasoa.server.classes')属性,该属性为允许远程调用的接口实现类名称(或类路径).如有多个可以","分隔.
  
  其中类名对应注解:@Service("exampleService")中定义的名称.

ServerStartupRunner.java

```java
    import org.springframework.stereotype.Component;
    import com.ikasoa.springboot.runner.IkasoaServerRunner;

    @Component
    public class ServerStartupRunner extends IkasoaServerRunner {
    }
```

  创建ServerStartupRunner类,ikasoa服务将会随spring-boot一起自动启动.

##### 客户端 #####

```java
    ......
    import org.springframework.beans.factory.annotation.Autowired;
    import com.ikasoa.springboot.IkasoaServiceProxy;
    import com.ikasoa.example.rpc.ExampleService;
    ......
    @Autowired
    IkasoaServiceProxy proxy;
    ......
    ExampleService es = proxy.getService(ExampleService.class);
    System.out.println(es.findVO(1).getString());
    ......
```

  如果调用远程服务,需在application.properties设置'ikasoa.server.host'和'ikasoa.server.port'属性,并与服务端匹配.

## License ##

*ikasoa is free to use under [MIT license](https://github.com/venwyhk/ikasoa/blob/master/LICENSE).*

***

*larry7696@gmail.com*&nbsp;&nbsp;[![](https://i.creativecommons.org/l/by/4.0/80x15.png)](http://creativecommons.org/licenses/by/4.0/)
