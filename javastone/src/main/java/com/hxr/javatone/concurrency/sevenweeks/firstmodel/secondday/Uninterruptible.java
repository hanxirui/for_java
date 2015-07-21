package com.hxr.javatone.concurrency.sevenweeks.firstmodel.secondday;

/**
 * {制造一个死锁} <br>
 * <p>
 * Create on : 2015年7月21日<br>
 * <p>
 * </p>
 * <br>
 * 
 * @author hanxirui<br>
 * @version javastone v1.0
 *          <p>
 *          <br>
 *          <strong>Modify History:</strong><br>
 *          user modify_date modify_content<br>
 *          -------------------------------------------<br>
 *          <br>
 */
public class Uninterruptible {
    public static void main(final String[] args) throws InterruptedException {
        final Object o1 = new Object();
        final Object o2 = new Object();
        Thread t1 = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (o1) {
                        Thread.sleep(1000);
                        synchronized (o2) {
                        }
                    }
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
                    synchronized (o2) {
                        Thread.sleep(1000);
                        synchronized (o1) {
                        }
                    }
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
