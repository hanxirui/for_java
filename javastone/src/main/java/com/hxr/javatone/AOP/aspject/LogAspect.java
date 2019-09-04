package com.hxr.javatone.AOP.aspject;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * Project: test-jar
 * Author: Kevin
 * Date: 16/8/14
 * Time: 上午10:41
 */
@Aspect
@Component
@Order(value = 1)
public class LogAspect {
    
    @Around("@annotation(aop.annotion.Log)")
    public void logImpl(ProceedingJoinPoint invocation) throws Exception{
        Object[] args = invocation.getArgs();
        ArrayList s = new ArrayList();
		for (int i = 0; i < args.length; i++) {
			s.add(args[i]);
		}
		if (args.length > 1) {
			return;
		}
		System.out.println("do log : args: " + s.toString());
        try {
            invocation.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }
}
