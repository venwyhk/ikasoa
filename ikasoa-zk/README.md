[![](https://raw.githubusercontent.com/venwyhk/ikasoa/master/ikasoalogo_small.png)](http://ikasoa.com)<br />

&nbsp;[![](https://codeship.com/projects/9cf2f150-1507-0134-ee57-3adebfc67210/status?branch=master)](https://codeship.com/projects/157977)&nbsp;&nbsp;[![Codacy Badge](https://api.codacy.com/project/badge/Grade/bd57bbda21bf4864aafcf230629140cd)](https://www.codacy.com/app/larry7696/ikasoa?utm_source=github.com&utm_medium=referral&utm_content=venwyhk/ikasoa&utm_campaign=Badge_Grade)&nbsp;<br/>&nbsp;[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.ikasoa/ikasoa-zk/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.ikasoa/ikasoa-zk)&nbsp;&nbsp;[![Javadocs](http://javadoc.io/badge/com.ikasoa/ikasoa-zk.svg)](http://javadoc.io/doc/com.ikasoa/ikasoa-zk)&nbsp;&nbsp;[![](https://img.shields.io/badge/license-MIT-097ABA.svg)](https://opensource.org/licenses/mit-license.php)&nbsp;&nbsp;

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
        <version>0.1</version>
    </dependency>
    ......
```

Gradle

```
    compile group: 'com.ikasoa', name: 'ikasoa-zk', version: '0.1'
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
