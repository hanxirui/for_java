package com.hxr.javatone.concurrency.sevenweeks.firstmodel.secondday;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Philosopher extends Thread {
    private final ReentrantLock leftChopstick, rightChopstick;
    private final Random random;

    public Philosopher(final ReentrantLock leftChopstick, final ReentrantLock rightChopstick) {
        this.leftChopstick = leftChopstick;
        this.rightChopstick = rightChopstick;
        random = new Random();
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(random.nextInt(1000));// 思考一段时间
                leftChopstick.lock();
                try {
                    if (rightChopstick.tryLock(1000, TimeUnit.MILLISECONDS)) {
                        // 获取右手边的筷子
                        try {
                            Thread.sleep(random.nextInt(1000));
                        } finally {
                            rightChopstick.unlock();
                        }
                    } else {
                        // 放弃，并继续思考
                    }
                } finally {
                    leftChopstick.unlock();
                }
            }
        } catch (InterruptedException e) {
        }

    }

}
