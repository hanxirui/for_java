package com.hxr.javatone.rpc.server;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;
//使用 RpcService 注解定义在服务接口的实现类上，需要对该实现类指定远程接口，因为实现类可能会实现多个接口，一定要告诉框架哪个才是远程接口。
//该注解具备 Spring 的 Component 注解的特性，可被 Spring 扫描。

//该实现类放在服务端 jar 包中，该 jar 包还提供了一些服务端的配置文件与启动服务的引导程序。
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component // 表明可被 Spring 扫描
public @interface RpcService {

    Class<?> value();
}