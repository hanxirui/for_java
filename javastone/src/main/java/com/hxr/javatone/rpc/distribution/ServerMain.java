package com.hxr.javatone.rpc.distribution;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ServerMain {
    public static void main(final String[] args) {
        new ClassPathXmlApplicationContext("spring-rpc-server.xml");
    }
}
