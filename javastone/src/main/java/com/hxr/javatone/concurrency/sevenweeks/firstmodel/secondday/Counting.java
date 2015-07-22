package com.hxr.javatone.concurrency.sevenweeks.firstmodel.secondday;

import java.util.concurrent.atomic.AtomicInteger;

public class Counting {
  public static void main(final String[] args) throws InterruptedException {
    
    final AtomicInteger counter = new AtomicInteger();
    
    class CountingThread extends Thread {
      @Override
    public void run() {
        for(int x = 0; x < 10000; ++x)
          counter.incrementAndGet();
      }
    }

    CountingThread t1 = new CountingThread();
    CountingThread t2 = new CountingThread();
    
    t1.start(); t2.start();
    t1.join(); t2.join();
    
    System.out.println(counter.get());
  }
}
