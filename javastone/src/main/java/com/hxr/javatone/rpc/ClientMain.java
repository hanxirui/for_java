package com.hxr.javatone.rpc;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hxr.javatone.rpc.client.RpcProxy;
import com.hxr.javatone.rpc.server.HelloService;

public class ClientMain {

    public static void main(final String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-rpc-client.xml");
        RpcProxy rpcProxy = (RpcProxy) context.getBean("rpcProxy");
        HelloService helloService = rpcProxy.create(HelloService.class);
        String result = helloService.hello("World");
        System.out.println("----" + result);
    }
}
