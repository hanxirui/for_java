package com.hxr.javatone.concurrency.java7concurrency.chapter2.recipe5;


/**
 * Main class of the example
 *
 */
public class Main {

	/**
	 * Main class of the example
	 * @param args
	 */
	public static void main(final String[] args) {

		// Creates an object to store the prices
		PricesInfo pricesInfo=new PricesInfo();
		
		Reader readers[]=new Reader[5];
		Thread threadsReader[]=new Thread[5];
		
		// Creates five readers and threads to run them
		for (int i=0; i<5; i++){
			readers[i]=new Reader(pricesInfo);
			threadsReader[i]=new Thread(readers[i]);
		}
		
		// Creates a writer and a thread to run it
		Writer writer=new Writer(pricesInfo);
		Thread threadWriter=new Thread(writer);
		
		// Starts the threads
		for (int i=0; i<5; i++){
			threadsReader[i].start();
		}
		threadWriter.start();
		
	}

}
