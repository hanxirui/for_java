package com.hxr.javatone.threadlocal;

/**
 * Created with IntelliJ IDEA.
 * Project: test-jar
 * Author: Kevin
 * Date: 2017/5/30
 * Time: 下午5:52
 */
public class TwoThreadLocalApp {

    ThreadLocal<Long>  rand1 = new ThreadLocal<Long>(){
        @Override
        protected Long initialValue() {
            return Thread.currentThread().getId();
        }
    };

    ThreadLocal<Long> rand2 = new ThreadLocal<Long>(){
        @Override
        protected Long initialValue() {
            return Thread.currentThread().getId()*2;
        }
    };

    public static void main(String[] args) {
        TwoThreadLocalApp twoThreadLocalApp = new TwoThreadLocalApp();
        System.out.println(twoThreadLocalApp.rand1.get());
        System.out.println(twoThreadLocalApp.rand2.get());
        /**
         * run 方法和  start方法的区别
         *
         * start方法才能真的 触发多线程特效
         * run实际还是一个线程同步的执行
         */
        start();
        run();

    }

    private static void start() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                TwoThreadLocalApp twoThreadLocalApp1 = new TwoThreadLocalApp();
                System.out.println(twoThreadLocalApp1.rand1.get());
                System.out.println(twoThreadLocalApp1.rand2.get());
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void run() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                TwoThreadLocalApp twoThreadLocalApp1 = new TwoThreadLocalApp();
                System.out.println(twoThreadLocalApp1.rand1.get());
                System.out.println(twoThreadLocalApp1.rand2.get());
            }
        };
        thread.run();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
