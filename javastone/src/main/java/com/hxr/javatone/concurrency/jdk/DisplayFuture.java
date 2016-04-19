package com.hxr.javatone.concurrency.jdk;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DisplayFuture {

	public static void main(String[] args) throws Exception {
		ExecutorService executor = Executors.newCachedThreadPool();
		System.out.println("Ready");
		Future strFuture = executor.submit(new CallableImpl());
		System.out.println("Give the future");

		System.out.println("Get the future : " + strFuture.get());
		System.out.println("End");
		executor.shutdown();
		// -----------------------
		System.out.println("-----------------------");
		ExecutorService es = Executors.newFixedThreadPool(1);
		System.out.println("Ready2");
		// Submits a Runnable task for execution and returns a Future
		// representing that task. The Future's get method will return null upon
		// successful completion.
		Future f2 = es.submit(new RunnableImpl());
		System.out.println("Give the future2");

		System.out.println("Get the future2 : " + f2.get());
		System.out.println("End2");
		es.shutdown();
	}

	public static class CallableImpl implements Callable {
		@Override
		public String call() throws Exception {
			Thread.sleep(2000);
			return "Hello World!";
		}
	}

	public static class RunnableImpl implements Runnable {
		private String hello;

		@Override
		public void run() {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			hello = "Hello World!";

		}

		public String getHello() {
			return hello;
		}
	}
}
