package com.hxr.javatone.threadlocal;

/**
 * Created with IntelliJ IDEA.
 * Project: test-jar
 * Author: Kevin
 * Date: 16/8/26
 * Time: 下午3:13
 */
public class ThreadLocalApp {

    public static void main(String[] args) throws Exception{
//        testShare();
        testThreadLocal();
    }

    public static void testThreadLocal() throws  Exception{
        for(int i = 0; i< 5; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    LocalValue localValue = new LocalValue();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(localValue.getValue());
                    localValue.increaseValue(10l);
                    System.out.println(localValue.getValue());
                }
            });
            thread.start();
            thread.join();
        }
    }

    public static void testShare() throws  Exception{
        for(int i = 0; i< 5; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    ShareValue shareValue = new ShareValue();
					System.out.println("thread name:" + Thread.currentThread().getName() + " " + shareValue.getValue());
					shareValue.increaseValue(10l);
					System.out.println("thread name:" + Thread.currentThread().getName() + " " + shareValue.getValue());
				}
            });
            thread.start();
//            thread.join();
        }
    }
}
