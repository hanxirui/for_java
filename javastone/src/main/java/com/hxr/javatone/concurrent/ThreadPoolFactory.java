package com.hxr.javatone.concurrent;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * Project: test-jar
 * Author: Kevin
 * Date: 2017/1/21
 * Time: 下午6:41
 */
public class ThreadPoolFactory {

    public static ThreadPoolExecutor getDefaultHandler(){
        /**
         * this queue is share for all core thread
         */
        return new ThreadPoolExecutor(3,10,1000L, TimeUnit.MICROSECONDS,new LinkedBlockingDeque<Runnable>(5));
    }

    public static ThreadPoolExecutor getCustomHandler(){
        return new ThreadPoolExecutor(3, 10, 1000L, TimeUnit.MICROSECONDS, new LinkedBlockingDeque<Runnable>(5), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                System.out.println("custom handler");
            }
        });
    }
}
