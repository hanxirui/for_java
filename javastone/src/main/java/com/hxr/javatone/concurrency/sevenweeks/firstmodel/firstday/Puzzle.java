package com.hxr.javatone.concurrency.sevenweeks.firstmodel.firstday;

//这段程序在我机器上执行有限多次的结果都是The meaning of life is: 42
//但是实际上有可能出现三种结果
//因为乱序执行是完全有可能发生的。
//编译器的静态优化可以打乱代码的执行顺序
//JVM的动态优化也会打乱代码的执行顺序
//硬件可以通过乱序执行来优化其性能
public class Puzzle {
    static boolean answerReady = false;
    static int answer = 0;
    static Thread t1 = new Thread() {
        @Override
        public void run() {
          answer = 42; 
          answerReady = true; 
        }
      };
    static Thread t2 = new Thread() {
        @Override
        public void run() {
          if (answerReady)
            System.out.println("The meaning of life is: " + answer);
          else
            System.out.println("I don't know the answer");
        }
      };

    public static void main(final String[] args) throws InterruptedException {
      t1.start(); t2.start();
      t1.join(); t2.join();
    }
  }
