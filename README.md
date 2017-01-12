![](https://raw.githubusercontent.com/venwyhk/ikasoa/master/ikasoalogo_small.png)<br />&nbsp;<b>Ika Service Oriented Architecture</b>

***

&nbsp;[![](https://codeship.com/projects/9cf2f150-1507-0134-ee57-3adebfc67210/status?branch=master)](https://codeship.com/projects/157977)&nbsp;&nbsp;[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.ikasoa/ikasoa-rpc/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/com.ikasoa/ikasoa-rpc)&nbsp;&nbsp;

</br>

## 概述 ##

  ikasoa是一套java分布式服务化治理解决方案,基于thrift框架开发.其中ikasoa-rpc是ikasoa的开源RPC框架,客户端可以像调用本地接口那样去调用远程接口.

## 开发运行环境要求 ##

- 要求java运行环境为java8

## 目录说明 ##

- ikasoa-admin : *利用Zookeeper注册和管理服务*

- ikasoa-core : *基础核心包*

- ikasoa-rpc : *实现RPC功能的代码*

- ikasoa-example : *示例代码*

## 环境搭建 ##

##### Maven配置 #####

  需要修改pom.xml文件,添加ikasoa-rpc的依赖:
    
pom.xml
```xml
    ......
    <dependency>
        <groupId>com.ikasoa</groupId>
        <artifactId>ikasoa-rpc</artifactId>
        <version>0.3-BETA3</version>
    </dependency>
    ......
```

##### Maven配置(ikasoa-core) #####

  如果仅使用thrift兼容方式,则可以只添加ikasoa-core依赖:

pom.xml
```xml
    ......
    <dependency>
        <groupId>com.ikasoa</groupId>
        <artifactId>ikasoa-core</artifactId>
        <version>0.4.7</version>
    </dependency>
    ......
```

##### 导入工程&编译代码 #####

  工程目录下命令行执行命令`mvn eclipse:eclipse`,并导入eclipse.(如果IDE非eclipse,则使用相对应的方式导入)

  执行命令`mvn clean package`打包.

## HelloWorld ##

##### 创建接口和实现 #####

  新建例子接口(ExampleService.java),对象(ExampleVO.java)和实现 (ExampleServiceImpl.java)类:

ExampleService.java
```java
    package com.ikamobile.ikasoa.example.rpc;
    public interface ExampleService {
        // 查询对象
        public ExampleVO findVO(int id);
    }
```

ExampleServiceImpl.java
```java
    package com.ikamobile.ikasoa.example.rpc;
    public class ExampleServiceImpl implements ExampleService {
        @Override
        public ExampleVO findVO(int id) {
            return new ExampleVO(id, "helloworld");
        }
    }
```

ExampleVO.java
```java
    package com.ikamobile.ikasoa.example.rpc;
    public class ExampleVO {
        private int id;
        private String string;
        public ExampleVO() {
        }
        public ExampleVO(int id, String string) {
            this.id = id;
            this.string = string;
        }
        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public String getString() {
            return string;
        }
        public void setString(String string) {
            this.string = string;
        }
    }
```

##### 服务端 #####

Server.java
```java
    import com.ikamobile.ikasoa.rpc.DefaultIkasoaFactory;
    import com.ikamobile.ikasoa.rpc.IkasoaException;
    import com.ikamobile.ikasoa.rpc.IkasoaServer;
    public class Server {
        private static IkasoaServer ikasoaServer;
        public static void start() {
            try {
                if (ikasoaServer == null) {
                    ikasoaServer = new DefaultIkasoaFactory().getIkasoaServer(ExampleServiceImpl.class, 9999);
                }
                // 启动服务
                ikasoaServer.run();
            } catch (IkasoaException e) {
                e.printStackTrace();
            }
        }
        public static void stop() {
            if (ikasoaServer != null && ikasoaServer.isServing()) {
                // 停止服务
                ikasoaServer.stop();
            }
        }
    }
```

##### 客户端 #####

Client.java
```java
    import com.ikamobile.ikasoa.rpc.DefaultIkasoaFactory;
    public class Client {
        public static void call() {
            // 客户端获取远程接口实现
            ExampleService es = new DefaultIkasoaFactory().getIkasoaClient(ExampleService.class, "localhost", 9999);
            // 客户端输出结果
            System.out.println(es.findVO(1).getString());
        }
    }
```

##### 执行类 #####

Main.java
```java
    public class Main {
        public static void main(String[] args) {
            try {
                // 启动服务
                Server.start();
                Thread.sleep(100);
                // 客户端调用
                Client.call();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 停止服务
                Server.stop();
            }
        }
    }
```

##### 执行 #####

  执行Main.java,或单独调用`Server.start()`启动服务后再调用`Client.call()`执行.

  如输出“helloworld”则表示执行成功.

  *可参考ikasoa-example的示例.*

## 使用示例 ##

  *此示例程序需要使用到Spring框架.*

##### 服务端例子 #####

bean.xml
```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context" xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.1.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.1.xsd">
        ......
        <!-- 服务端配置 -->
        <bean id="rpcServer" class="example.ikasoa.RpcServer" init-method="run" destroy-method="stop">
            <constructor-arg index="0" ref="ikasoaFactory"/>
            <constructor-arg index="1">
                <value>9993</value><!-- 设置服务开放端口 -->
            </constructor-arg>
        </bean>
        <bean id="ikasoaFactory" class="com.ikamobile.ikasoa.rpc.DefaultIkasoaFactory"/>
        ......
    </beans>
```

RpcServer.java
```java
    package example.ikasoa;
    import com.ikamobile.ikasoa.rpc.IkasoaException;
    import com.ikamobile.ikasoa.rpc.IkasoaFactory;
    import com.ikamobile.ikasoa.rpc.IkasoaServer;
    public class RpcServer {
        private IkasoaServer server;
        public RpcServer(IkasoaFactory ikasoaFactory, int serverPort) throws IkasoaException {
            // 实现类不能是抽象类
            this.server = ikasoaFactory.getIkasoaServer(ExampleServiceImpl.class, serverPort);
            // 如果已有实例化后的对象(例如通过Spring注入的对象),则可以通过ImplClsCon类进行封装,ikasoa-rpc将会直接引用该类的实例,而不会重新实例化.
            // 例子如下:
            // this.server = ikasoaFactory.getIkasoaServer(new ImplClsCon(ExampleServiceImpl.class, exampleServiceImpl), serverPort);
            // 如有多个接口实现,可以传入List.
            // 例子如下:
            // List<ImplClsCon> sList = new ArrayList<ImplClsCon>();
            // sList.add(new ImplClsCon(ExampleServiceImpl.class));
            // sList.add(new ImplClsCon(Example2ServiceImpl.class));
            // this.server = ikasoaFactory.getIkasoaServer(sList, serverPort);
            System.out.println("服务端口:" + serverPort);
            for (String key : this.server.getIkasoaServiceKeys()) {
                System.out.println("加载服务:" + key);
            }
        }
        public void run() {
            server.run();
        }
        public void stop() {
            server.stop();
        }
    }
```

##### 客户端例子 #####

RpcClient.java
```java
    package example.ikasoa;
    import com.ikamobile.ikasoa.rpc.DefaultIkasoaFactory;
    public class RpcClient {
        public static void main(String[] args) {
            // 如果接口之间有继承关系,则只需要配置子接口类
            // 设置服务器地址为”hocalhost”,端口为9993
            ExampleService es = new DefaultIkasoaFactory().getIkasoaClient(ExampleService.class, "localhost", 9993);
            // 如果有多个服务提供者,服务器地址和端口也可以传入List,系统将自动执行负载均衡(默认负载均衡规则为轮询,此外还支持随机,详见'负载均衡'文档目录).
            // 例子如下:
            //  List<ServerInfo> serverInfoList = new ArrayList<ServerInfo>();
            //  serverInfoList.add(new ServerInfo("localhost", 9993));
            //  serverInfoList.add(new ServerInfo("192.168.1.41", 9993));
            //  ExampleService es = new DefaultIkasoaFactory().getIkasoaClient(ExampleService.class, serverInfoList);
            System.out.println(es.findVO(1).getString());
        }
    }
```

##### 执行RpcClient.java #####

  如输出”helloworld”则表示执行成功.

## Thrift使用示例 ##

##### 客户端调用Thrift服务端例子 #####

ThriftClientDemo.java
```java
    package example.ikasoa;
    import org.apache.thrift.transport.TTransport;
    import org.apache.thrift.transport.TTransportFactory;
    import com.ikamobile.ikasoa.core.thrift.client.ThriftClient;
    import com.ikamobile.ikasoa.core.thrift.client.ThriftClientConfiguration;
    import com.ikamobile.ikasoa.rpc.DefaultIkasoaFactory;
    import com.ikamobile.tmcs.controller.thrift.server.acceptor.GeneralThriftAcceptor;
    public class ThriftClientDemo {
        public static void main(String[] args) {
            ThriftClientConfiguration configuration = new ThriftClientConfiguration();
            configuration.setTransportFactory(new TTransportFactory()); // 协议需要与服务端匹配
            // 如果只依赖ikasoa-core,这里也可以使用com.ikamobile.ikasoa.core.thrift.GeneralFactory来替代DefaultIkasoaFactory
            ThriftClient thriftClient = new DefaultIkasoaFactory(configuration).getThriftClient("121.40.119.240", 9201); // 配置Thrift的服务器地址和端口
            TTransport transport = null;
            try {
                transport = thriftClient.getTransport();
                transport.open();
                // GeneralThriftAcceptor为ThriftIDL配置所生成的service
                GeneralThriftAcceptor.Client client = new GeneralThriftAcceptor.Client(
                thriftClient.getProtocol(transport, "GeneralThriftAcceptor")); // 参数"GeneralThriftAcceptor"为服务的key,如果没有则可以不传
                // 打印结果
                System.out.println(client.getTmc(1));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                transport.close();
            }
        }
    }
```

##### Spring配置Thrift服务端例子 #####

```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context" xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-2.5.xsd">
        ......
        <!-- Thrift服务配置 -->
        <bean id="thriftServer2" class="com.ikamobile.ikasoa.core.thrift.server.impl.DefaultThriftServerImpl" init-method="run" destroy-method="stop">
            <property name="serverName" value="xxxServer" /><!-- 服务名称 -->
            <property name="serverPort" value="9899" /><!-- 服务端口 -->
            <property name="thriftServerConfiguration">
                <bean class="com.ikamobile.ikasoa.core.thrift.server.ThriftServerConfiguration">
                    <property name="transportFactory"><!-- 指定传输协议工厂(可选,默认为TFramedTransport.Factory) -->
                        <bean class="org.apache.thrift.transport.TTransportFactory" />
                    </property>
                </bean>
            </property>
            <property name="processor">
                <bean class="com.ikamobile.xxx.service.ThriftService.Processor"><!-- ThriftService为通过idl生成的服务类 -->
                    <constructor-arg ref="thriftService" />
                </bean>
            </property>
        </bean>
        <bean id="thriftService" class="com.ikamobile.xxx.ThriftServiceImpl"/><!-- ThriftService.Iface接口的实现 -->
        ......
    </beans>
```

##### Spring配置Thrift服务端例子(嵌套方式) #####

```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context" xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-2.5.xsd">
        ......
        <!-- Thrift服务配置(嵌套方式) -->
        <bean id="thriftServer1" class="com.ikamobile.ikasoa.core.thrift.server.impl.DefaultThriftServerImpl" init-method="run" destroy-method="stop">
            <property name="serverName" value="xxxServer" /><!-- 服务名称 -->
            <property name="serverPort" value="9898" /><!-- 服务端口 -->
            <property name="thriftServerConfiguration">
                <bean class="com.ikamobile.ikasoa.core.thrift.server.ThriftServerConfiguration">
                    <property name="transportFactory"><!-- 指定传输协议工厂(可选,默认为TFramedTransport.Factory) -->
                        <bean class="org.apache.thrift.transport.TTransportFactory" />
                    </property>
                </bean>
            </property>
            <property name="processor">
                <bean class="com.ikamobile.ikasoa.core.thrift.server.MultiplexedProcessor">
                    <constructor-arg>
                        <map>
                            <entry key="Service1"><!-- 这里的key可以随便取,保证唯一就行,Client调用的时候需要用 -->
                                <bean class="com.ikamobile.xxx.service.ThriftService1.Processor"><!-- ThriftService1和ThriftService2为通过idl生成的服务类 -->
                                    <constructor-arg ref="thriftService1" />
                                </bean>
                            </entry>
                            <entry key="Service2">
                                <bean class="com.ikamobile.xxx.service.ThriftService2.Processor">
                                    <constructor-arg ref="thriftService2" />
                                </bean>
                            </entry>
                        </map>
                    </constructor-arg>
                </bean>
            </property>
        </bean>
        <bean id="thriftService1" class="com.ikamobile.xxx.ThriftService1Impl"/><!-- ThriftService1.Iface接口的实现 -->
        <bean id="thriftService2" class="com.ikamobile.xxx.ThriftService2Impl"/><!-- ThriftService2.Iface接口的实现 -->
        ......
    </beans>
```

## 服务实现类型 ##

  *ikasoa默认使用Thrift作为服务类型的实现,但也提供了Netty以供选择.*

##### 使用Thrift服务 #####

```java
    ......
    IkasoaFactory ikasoaFactory = new DefaultIkasoaFactory();
    ......
```

##### 使用Netty服务 #####

```java
    ......
    IkasoaFactory ikasoaFactory = new NettyIkasoaFactory();
    ......
```

## 阻塞式IO与非阻塞式IO ##

  *ikasoa服务端默认使用的是阻塞式IO,在高并发场景中建议改为非阻塞式IO方式.*

##### 使用非阻塞式IO #####

```java
    ......
    Configurator configurator = new Configurator();
    configurator.setNonBlockingIO(true);
    IkasoaFactory ikasoaFactory = new DefaultIkasoaFactory(configurator);
    ......
```

## 负载均衡 ##

  *ikasoa提供了2种负载均衡,分别为轮循(含权重)和随机,默认使用轮循.*

##### 使用轮循负载均衡(默认) #####

```java
    ......
    XService xs = new DefaultIkasoaFactory().getIkasoaClient(XService.class, serverInfoList);
    // 也可以写为如下方式:
    // Class loadBalanceClass = Class.forName("com.ikamobile.ikasoa.core.loadbalance.impl.PollingLoadBalanceImpl");
    // XService xs = new DefaultIkasoaFactory().getIkasoaClient(XService.class, serverInfoList, loadBalanceClass);
    ......
```

  *serverInfoList中的元素对象`com.ikamobile.ikasoa.core.loadbalance.ServerInfo`定义了单个服务信息,其中`weightNumber`属性为权重值,用于轮循负载均衡.*

##### 使用随机负载均衡 #####

```java
    ......
    Class loadBalanceClass = Class.forName("com.ikamobile.ikasoa.core.loadbalance.impl.RandomLoadBalanceImpl");
    XService xs = new DefaultIkasoaFactory().getIkasoaClient(XService.class, serverInfoList, loadBalanceClass);
    ......
```

##### 自定义负载均衡 #####

  创建自定义序列化类(例如com.xxx.XLoadBalanceImpl).

  自定义序列化类(com.xxx.XLoadBalanceImpl)需实现接口`com.ikamobile.ikasoa.core.loadbalance.LoadBalance`.

  通过如下方式获取服务:

```java
    ......
    Class loadBalanceClass = Class.forName("com.xxx.XLoadBalanceImpl");
    XService xs = new DefaultIkasoaFactory().getIkasoaClient(XService.class, serverInfoList, loadBalanceClass);
    ......
```

## 序列化 ##

  *ikasoa提供了3种序列化方式,分别为fastjson,xml,kryo,默认使用fastjson.*

##### 使用fastjson作为序列化方式(默认) #####

```java
    ......
    IkasoaFactory ikasoaFactory = new DefaultIkasoaFactory();
    // 也可以写为如下方式:
    // Class protocolHandlerClass = Class.forName("com.ikamobile.ikasoa.rpc.handler.impl.JsonProtocolHandlerImpl");
    // IkasoaFactory ikasoaFactory = new DefaultIkasoaFactory(new Configurator(protocolHandlerClass));
    ......
```

##### 使用xml作为序列化方式 #####

```java
    ......
    Class protocolHandlerClass = Class.forName("com.ikamobile.ikasoa.rpc.handler.impl.XmlProtocolHandlerImpl");
    IkasoaFactory ikasoaFactory = new DefaultIkasoaFactory(new Configurator(protocolHandlerClass));
    ......
```

##### 使用kryo作为序列化方式 #####

```java
    ......
    Class protocolHandlerClass = Class.forName("com.ikamobile.ikasoa.rpc.handler.impl.KryoProtocolHandlerImpl");
    IkasoaFactory ikasoaFactory = new DefaultIkasoaFactory(new Configurator(protocolHandlerClass));
    ......
```

##### 自定义序列化方式 #####

  创建自定义序列化类(例如com.xxx.XProtocolHandlerImpl).

  自定义序列化类(com.xxx.XProtocolHandlerImpl)需实现接口`com.ikamobile.ikasoa.rpc.handler.ProtocolHandler`.

  通过如下方式获取IkasoaFactory:

```java
    ......
    Class protocolHandlerClass = Class.forName("com.xx.XProtocolHandlerImpl");
    IkasoaFactory ikasoaFactory = new DefaultIkasoaFactory(new Configurator(protocolHandlerClass));
    ......
```

## 注意事项 ##

- 使用fastjson作为序列化方式时,fastjson依赖版本建议与ikasoa所依赖的版本一致.否则可能出现服务名不能匹配,无法调用服务的情况.

- 使用kryo作为序列化方式时,如果参数或返回值以父类(或抽象类)的形式传递,转换为子类时可能会丢失子类属性值,建议尽量以子类形式传递参数.fastjson方式非集合类参数或返回值没有问题,xml方式都没有问题.

- 使用fastjson作为序列化方式时,传递的Bean对象必须要有默认构造方法(建议使用类似lombok这样的工具来处理Bean对象).

- 使用kryo作为序列化方式时,暂不支持自定义异常对象,如果抛出自定义异常对象,异常类型不能正确识别.

- 部分单元测试需要启动Socket,执行单元测试时请确保相关端口可用.

## License ##

  *ikasoa is free to use under [MIT license](https://github.com/venwyhk/ikasoa/blob/master/LICENSE).*

***

*larry7696@gmail.com*
