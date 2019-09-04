package com.hxr.javatone.concurrent;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created with IntelliJ IDEA.
 * Project: test-jar
 * Author: Kevin
 * Date: 2017/1/21
 * Time: 下午6:39
 */
public class MainApp {

    public static void main(String[] args)  throws Exception{
        ThreadPoolExecutor defaultHandler = ThreadPoolFactory.getDefaultHandler();
        while (true) {
            defaultHandler.submit(new Task());
            Thread.sleep(10);
        }

//        ThreadPoolExecutor customHandler = ThreadPoolFactory.getCustomHandler();
//        while (true) {
//            customHandler.submit(new Task());
//            Thread.sleep(10);
//        }
    }
}


