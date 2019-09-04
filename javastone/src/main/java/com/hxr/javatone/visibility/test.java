package com.hxr.javatone.visibility;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA. Project: test-jar Author: Kevin Date:
 * 16/5/23 Time: 下午2:58
 */

public class test {

	private static boolean is = true;

	public static void main(String[] args) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				int i = 0;
				while (test.is) {

					i++;
					// 会强制刷新主内存的变量值到线程栈?
					// synchronized (this) { }

//					System.out.println("1");
					// println 是synchronized的,会强制刷新主内存的变量值到线程栈?
					// sleep 会从新load主内存的值?
					try {
						TimeUnit.MICROSECONDS.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				is = false; // 设置is为false，使上面的线程结束while循环
			}
		}).start();
	}
}
