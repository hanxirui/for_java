package com.hxr.javatone.rpc.distribution.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;
//第四步：启动服务器并发布服务

//为了加载 Spring 配置文件来发布服务，只需编写一个引导程序即可：
//运行 RpcBootstrap 类的 main 方法即可启动服务端，但还有两个重要的组件尚未实现，它们分别是： ServiceRegistry 与 RpcServer
public class RpcBootstrap {

    public static void main(final String[] args) {
        new ClassPathXmlApplicationContext("spring-rpc-server.xml");
    }
}