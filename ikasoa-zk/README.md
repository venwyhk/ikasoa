[![](http://ikasoa.com/ikasoalogo_small.png)](http://ikasoa.com)<br />

&nbsp;[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.ikasoa/ikasoa-zk/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.ikasoa/ikasoa-zk)&nbsp;&nbsp;[![Javadocs](http://javadoc.io/badge/com.ikasoa/ikasoa-zk.svg)](http://javadoc.io/doc/com.ikasoa/ikasoa-zk)&nbsp;&nbsp;

***

# ikasoa zk #

## 概述 ##

  ikasoa利用zookeeper注册和更新服务.

## 开发运行环境要求 ##

- 要求java运行环境为java8

## 环境搭建 ##

##### 引用依赖包 #####

Maven

pom.xml

```xml
    ......
    <dependency>
        <groupId>com.ikasoa</groupId>
        <artifactId>ikasoa-zk</artifactId>
        <version>0.1.1-BETA</version>
    </dependency>
    ......
```

Gradle

```
    compile group: 'com.ikasoa', name: 'ikasoa-zk', version: '0.1.1-BETA'
```

## 使用示例 ##

##### 服务端(服务注册) #####

```java
    ......
    ThriftServerConfiguration thriftServerConfiguration = new ThriftServerConfiguration();
    thriftServerConfiguration.setServerAspect(new ZkServerAspect("xxx.xxx.xxx.xxx:2181", null));
    ......
```

##### 客户端(服务发现) #####

```java
    ......
    ThriftClientConfiguration thriftClientConfiguration = new ThriftClientConfiguration();
    thriftClientConfiguration.setServerCheck(new ZkServerCheck("xxx.xxx.xxx.xxx:2181", null));
    ......
```

## License ##

*ikasoa is free to use under [MIT license](https://github.com/venwyhk/ikasoa/blob/master/LICENSE).*

***

*larry7696@gmail.com*&nbsp;&nbsp;[![](https://i.creativecommons.org/l/by/4.0/80x15.png)](http://creativecommons.org/licenses/by/4.0/)
