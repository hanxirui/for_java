package com.hxr.javatone.concurrency.java7concurrency.chapter2.recipe4;


/**
 * Main class of the example.
 *
 */
public class Main {

	/**
	 * Main method of the class. Run ten jobs in parallel that
	 * send documents to the print queue at the same time.
	 */
	public static void main (final String args[]){
		
		// Creates the print queue
		PrintQueue printQueue=new PrintQueue();
		
		// Creates ten Threads
		Thread thread[]=new Thread[10];
		for (int i=0; i<10; i++){
			thread[i]=new Thread(new Job(printQueue),"Thread "+i);
		}
		
		// Starts the Threads
		for (int i=0; i<10; i++){
			thread[i].start();
		}
	}

}
