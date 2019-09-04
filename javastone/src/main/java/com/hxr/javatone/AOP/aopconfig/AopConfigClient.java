package com.hxr.javatone.AOP.aopconfig;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * Project: test-jar
 * Author: Kevin
 * Date: 16/8/15
 * Time: 下午3:17
 */
public class AopConfigClient {

    public static void main(String[] args) {
        xmlTest();
    }

    private static void xmlTest(){
        ApplicationContext context = new ClassPathXmlApplicationContext("config/spring/local/appcontext-core.xml"); // 获取 Spring Context
        AopConfigInterface action = (AopConfigInterface) context.getBean("impl1");                        // 从 Context 中根据 id 获取 Bean 对象（其实就是一个代理）
        action.add("object");

        System.out.println("__________");
        action.print("hello");
    }



}
