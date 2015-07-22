package com.hxr.javatone.concurrency.sevenweeks.firstmodel.secondday;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * {可重入锁支持条件变量} <br>
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
public class PhilosopherCondition extends Thread {

    private boolean eating;
    private PhilosopherCondition left;
    private PhilosopherCondition right;
    private final ReentrantLock table;
    private final Condition condition;
    private int thinkCount;

    public PhilosopherCondition(final ReentrantLock table) {
        eating = false;
        this.table = table;
        condition = table.newCondition();
    }

    public void setLeft(final PhilosopherCondition left) {
        this.left = left;
    }

    public void setRight(final PhilosopherCondition right) {
        this.right = right;
    }

    @Override
    public void run() {
        try {
            while (true) {
                think();
                eat();
            }
        } catch (InterruptedException e) {
        }
    }

    private void think() throws InterruptedException {
        table.lock();
        try {
            eating = false;
            left.condition.signal();
            right.condition.signal();
        } finally {
            table.unlock();
        }
        ++thinkCount;
        if (thinkCount % 10 == 0)
            System.out.println("PhilosopherCondition " + this + " has thought " + thinkCount + " times");
        Thread.sleep(1000);
    }

    private void eat() throws InterruptedException {
        table.lock();
        try {
            while (left.eating || right.eating)
                condition.await();
            eating = true;
        } finally {
            table.unlock();
        }
        Thread.sleep(1000);
    }

    public static void main(final String[] args) throws InterruptedException {
        PhilosopherCondition[] PhilosopherConditions = new PhilosopherCondition[5];
        ReentrantLock table = new ReentrantLock();

        for (int i = 0; i < 5; ++i)
            PhilosopherConditions[i] = new PhilosopherCondition(table);
        for (int i = 0; i < 5; ++i) {
            PhilosopherConditions[i].setLeft(PhilosopherConditions[(i + 4) % 5]);
            PhilosopherConditions[i].setRight(PhilosopherConditions[(i + 1) % 5]);
            PhilosopherConditions[i].start();
        }
        for (int i = 0; i < 5; ++i)
            PhilosopherConditions[i].join();
    }
}