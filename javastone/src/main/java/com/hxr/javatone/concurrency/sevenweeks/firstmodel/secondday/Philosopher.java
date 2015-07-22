package com.hxr.javatone.concurrency.sevenweeks.firstmodel.secondday;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * {演示可重入锁的超时机制}
 * <br>
 *  
 * <p>
 * Create on : 2015年7月21日<br>
 * <p>
 * </p>
 * <br>
 * @author hanxirui<br>
 * @version javastone v1.0
 * <p>
 *<br>
 * <strong>Modify History:</strong><br>
 * user     modify_date    modify_content<br>
 * -------------------------------------------<br>
 * <br>
 */
public class Philosopher extends Thread {
    private final ReentrantLock leftChopstick, rightChopstick;
    private final Random random;
    private int thinkCount;

    public Philosopher(final ReentrantLock leftChopstick, final ReentrantLock rightChopstick) {
      this.leftChopstick = leftChopstick; this.rightChopstick = rightChopstick;
      random = new Random();
    }

    @Override
    public void run() {
      try {
        while(true) {
          ++thinkCount;
          if (thinkCount % 10 == 0)
            System.out.println("Philosopher " + this + " has thought " + thinkCount + " times");
          Thread.sleep(random.nextInt(1000)); // Think for a while
          leftChopstick.lock();
          try {
            if (rightChopstick.tryLock(1000, TimeUnit.MILLISECONDS)) {
              // Got the right chopstick
              try {
                Thread.sleep(random.nextInt(1000)); // Eat for a while
              } finally { rightChopstick.unlock(); }
            } else {
              // Didn't get the right chopstick - give up and go back to thinking
              System.out.println("Philosopher " + this + " timed out");
            }
          } finally { leftChopstick.unlock(); }
        }
      } catch(InterruptedException e) {}
    }

    public static void main(final String[] args) throws InterruptedException {
        Philosopher[] philosophers = new Philosopher[5];
        ReentrantLock[] chopsticks = new ReentrantLock[5];
        
        for (int i = 0; i < 5; ++i)
          chopsticks[i] = new ReentrantLock();
        for (int i = 0; i < 5; ++i) {
          philosophers[i] = new Philosopher(chopsticks[i], chopsticks[(i + 1) % 5]);
          philosophers[i].start();
        }
        for (int i = 0; i < 5; ++i)
          philosophers[i].join();
      }
}
