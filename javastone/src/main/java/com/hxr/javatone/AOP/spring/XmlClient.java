package com.hxr.javatone.AOP.spring;

import com.hxr.javatone.AOP.Action;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * Project: test-jar
 * Author: Kevin
 * Date: 16/8/15
 * Time: 下午3:17
 */
public class XmlClient {

    public static void main(String[] args) {
        xmlTest();
//        introductionAdvice();
    }

    private static void xmlTest(){
        ApplicationContext context = new ClassPathXmlApplicationContext("config/spring/local/appcontext-core.xml"); // 获取 Spring Context
        Action action = (Action) context.getBean("actionProxy");                        // 从 Context 中根据 id 获取 Bean 对象（其实就是一个代理）
        action.add("object");
    }

    private static void introductionAdvice(){
        ApplicationContext context = new ClassPathXmlApplicationContext("config/spring/local/appcontext-core.xml"); // 获取 Spring Context
        Action action = (Action) context.getBean("introductionProxy");                        // 从 Context 中根据 id 获取 Bean 对象（其实就是一个代理）
        action.add("object");
    }


}
