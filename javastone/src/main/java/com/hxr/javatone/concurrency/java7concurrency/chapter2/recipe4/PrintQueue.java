package com.hxr.javatone.concurrency.java7concurrency.chapter2.recipe4;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class simulates a print queue
 *
 */
public class PrintQueue {

	/**
	 * Lock to control the access to the queue.
	 */
	private final Lock queueLock=new ReentrantLock();
	
	/**
	 * Method that prints a document
	 * @param document document to print
	 */
	public void printJob(final Object document){
		queueLock.lock();
		
		try {
			Long duration=(long)(Math.random()*10000);
			System.out.printf("%s: PrintQueue: Printing a Job during %d seconds\n",Thread.currentThread().getName(),(duration/1000));
			Thread.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			queueLock.unlock();
		}
	}
}
