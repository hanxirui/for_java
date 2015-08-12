package com.hxr.javatone.concurrency.java7concurrency.chapter2.recipe7;

import java.util.Random;

/**
 * This class reads line from the buffer and process it
 *
 */
public class Consumer implements Runnable {

	/**
	 * The buffer
	 */
	private final Buffer buffer;
	
	/**
	 * Constructor of the class. Initialize the buffer
	 * @param buffer
	 */
	public Consumer (final Buffer buffer) {
		this.buffer=buffer;
	}
	
	/**
	 * Core method of the consumer. While there are pending lines in the
	 * buffer, try to read one.
	 */
	@Override
	public void run() {
		while (buffer.hasPendingLines()) {
			String line=buffer.get();
			processLine(line);
		}
	}

	/**
	 * Method that simulates the processing of a line. Waits 10 milliseconds
	 * @param line
	 */
	private void processLine(final String line) {
		try {
			Random random=new Random();
			Thread.sleep(random.nextInt(100));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}

}
