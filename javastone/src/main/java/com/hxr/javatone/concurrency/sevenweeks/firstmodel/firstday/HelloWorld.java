package com.hxr.javatone.concurrency.sevenweeks.firstmodel.firstday;

public class HelloWorld {

    public static void main(final String[] args) throws InterruptedException {
        Thread myThread = new Thread() {
            @Override
            public void run() {
                System.out.println("Hello from new thread");
            }
        };

        myThread.start();
        // yield的使用让主线程和myThread处于同一起跑线上，从而两种打印结果都有可能出现；如果不使用yield，则打印结果总是主线程的。
        // yield可以用于调试或测试目的，有助于重现由于竞争条件导致的一些问题。
        Thread.yield();
        System.out.println("Hello from main thread");
        myThread.join();
    }
}
