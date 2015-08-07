package com.hxr.javatone.rpc.server;

//第二步：编写服务接口的实现类
@RpcService(HelloService.class) // 指定远程接口
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(final String name) {
        return "Hello! " + name;
    }
}
