package com.hxr.javatone.concurrency.jdk;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

//Another typical usage would be to divide a problem into N parts, 
//describe each part with a Runnable that executes that portion and counts down on the latch, 
//and queue all the Runnables to an Executor. When all sub-parts are complete, 
//the coordinating thread will be able to pass through await. 
//(When threads must repeatedly count down in this way, instead use a CyclicBarrier.)

public class CountDownDemo2 {

    public static void main(final String[] args) throws InterruptedException {
        int N = 10;

        // 一个完成就完成
        // CountDownLatch doneSignal = new CountDownLatch(1);
        // 全部完成才完成
        CountDownLatch doneSignal = new CountDownLatch(N);
        Executor e = Executors.newFixedThreadPool(N);

        for (int i = 0; i < N; ++i)
            // create and start threads
            e.execute(new WorkerRunnable(doneSignal, i));

        doneSignal.await(); // wait for all to finish
        System.out.println("---finish");
    }

    // Memory consistency effects: Until the count reaches zero, actions in a thread prior to calling countDown()
    // happen-before actions following a successful return from a corresponding await() in another thread.

}

class WorkerRunnable implements Runnable {
    private final CountDownLatch doneSignal;
    private final int i;

    WorkerRunnable(final CountDownLatch doneSignal, final int i) {
        this.doneSignal = doneSignal;
        this.i = i;
    }

    @Override
    public void run() {
        doWork(i);
        doneSignal.countDown();
    }

    void doWork(final int i) {
        try {
            Thread.sleep(1000 * 3);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("------------" + i);
    }
}
