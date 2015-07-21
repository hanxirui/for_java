package com.hxr.javatone.concurrency.sevenweeks.firstmodel.secondday;

import java.util.concurrent.locks.ReentrantLock;

public class Interruptible {
    public static void main(final String[] args) throws InterruptedException {

        final ReentrantLock l1 = new ReentrantLock();
        final ReentrantLock l2 = new ReentrantLock();

        Thread t1 = new Thread() {
            @Override
            public void run() {
                try {
                    l1.lockInterruptibly();
                    Thread.sleep(1000);
                    l2.lockInterruptibly();
                    System.out.println("t1 is running.");
                } catch (InterruptedException e) {
                    System.out.println("t1 interrupted");
                }
            }

        };
        Thread t2 = new Thread() {
            @Override
            public void run() {
                try {
                    l2.lockInterruptibly();
                    Thread.sleep(1000);
                    l1.lockInterruptibly();
                    System.out.println("t2 is running.");
                } catch (InterruptedException e) {
                    System.out.println("t2 interrupted");
                }
            }

        };
        t1.start();
        t2.start();
        Thread.sleep(2000);
        t1.interrupt();
        t2.interrupt();
        t1.join();
        t2.join();

    }
}
