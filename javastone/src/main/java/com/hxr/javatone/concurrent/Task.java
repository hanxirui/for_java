package com.hxr.javatone.concurrent;

/**
 * Created with IntelliJ IDEA.
 * Project: test-jar
 * Author: Kevin
 * Date: 2017/1/21
 * Time: 下午6:46
 */
public class Task implements Runnable {

    @Override
    public void run() {
        try {
            //handle some thing long time
			System.out.println("task is running");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
