package com.hxr.javatone.rpc.distribution.server;

//第一步：编写服务接口
//该接口同时需要放在独立的客户端 jar 包中，以供应用使用。
public interface HelloService {

    String hello(String name);
}
