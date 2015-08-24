package com.hxr.javatone.rpc.simple;

public class HelloServiceImpl implements HelloServiceInterface {

    @Override
    public String hello(final String name) {
        return "Hello " + name;  
    }

}
