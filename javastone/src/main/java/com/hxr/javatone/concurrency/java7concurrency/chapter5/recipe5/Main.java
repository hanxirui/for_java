package com.hxr.javatone.concurrency.java7concurrency.chapter5.recipe5;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * Main class of the program. 
 */
public class Main {

	/**
	 * Main method of the example
	 * @param args
	 */
	public static void main(final String[] args) {

		// Generate an array of 1000 integers
		ArrayGenerator generator=new ArrayGenerator();
		int array[]=generator.generateArray(1000);
		
		// Create a TaskManager object
		TaskManager manager=new TaskManager();
		
		// Create a ForkJoinPool with the default constructor
		ForkJoinPool pool=new ForkJoinPool();
		
		// Create a Task to process the array
		SearchNumberTask task=new SearchNumberTask(array,0,1000,5,manager);
		
		// Execute the task
		pool.execute(task);

		// Shutdown the pool
		pool.shutdown();
		
		
		// Wait for the finalization of the task
		try {
			pool.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Write a message to indicate the end of the program
		System.out.printf("Main: The program has finished\n");
	}

}
