package com.hxr.javatone.concurrency.sevenweeks.firstmodel.secondday;


import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 * {可以对部分加锁}
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
public class ConcurrentSortedList {

  private class Node {
    int value;
    Node prev;
    Node next;
    ReentrantLock lock = new ReentrantLock();

    Node() {}

    Node(final int value, final Node prev, final Node next) {
      this.value = value; this.prev = prev; this.next = next;
    }
  }

  private final Node head;
  private final Node tail;

  public ConcurrentSortedList() {
    head = new Node(); tail = new Node();
    head.next = tail; tail.prev = head;
  }

  public void insert(final int value) {
    Node current = head;
    current.lock.lock(); 
    Node next = current.next;
    try {
      while (true) {
        next.lock.lock(); 
        try {
          if (next == tail || next.value < value) { 
            Node node = new Node(value, current, next); 
            next.prev = node;
            current.next = node;
            return; 
          }
        } finally { current.lock.unlock(); } 
        current = next;
        next = current.next;
      }
    } finally { next.lock.unlock(); } 
  }

  public int size() {
    Node current = tail;
    int count = 0;
    
    while (current.prev != head) {
      ReentrantLock lock = current.lock;
      lock.lock();
      try {
        ++count;
        current = current.prev;
      } finally { lock.unlock(); }
    }
    
    return count;
  }

  public boolean isSorted() {
    Node current = head;
    while (current.next.next != tail) {
      current = current.next;
      if (current.value < current.next.value)
        return false;
    }
    return true;
  }

  public static void main(final String[] args) throws InterruptedException {
    final ConcurrentSortedList list = new ConcurrentSortedList();
    final Random random = new Random();

    class TestThread extends Thread {
      @Override
    public void run() {
        for (int i = 0; i < 10000; ++i)
          list.insert(random.nextInt());
      }
    }

    class CountingThread extends Thread {
      @Override
    public void run() {
        while (!interrupted()) {
          System.out.print("\r" + list.size());
          System.out.flush();
        }
      }
    }

    Thread t1 = new TestThread();
    Thread t2 = new TestThread();
    Thread t3 = new CountingThread();

    t1.start(); t2.start(); t3.start();
    t1.join(); t2.join();
    t3.interrupt();

    System.out.println("\r" + list.size());

    if (list.size() != 20000)
      System.out.println("*** Wrong size!");

    if (!list.isSorted())
      System.out.println("*** Not sorted!");
  }
}
