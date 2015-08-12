package com.hxr.javatone.concurrency.java7concurrency.chapter2.recipe7;


/**
 * This class gets lines from the simulate file and stores them in the
 * buffer, if there is space in it.
 *
 */
public class Producer implements Runnable {

	/**
	 * Simulated File
	 */
	private final FileMock mock;
	
	/**
	 * Buffer
	 */
	private final Buffer buffer;
	
	/**
	 * Constructor of the class. Initialize the objects
	 * @param mock Simulated file
	 * @param buffer Buffer
	 */
	public Producer (final FileMock mock, final Buffer buffer){
		this.mock=mock;
		this.buffer=buffer;	
	}
	
	/**
	 * Core method of the producer. While are pending lines in the
	 * simulated file, reads one and try to store it in the buffer.
	 */
	@Override
	public void run() {
		buffer.setPendingLines(true);
		while (mock.hasMoreLines()){
			String line=mock.getLine();
			buffer.insert(line);
		}
		buffer.setPendingLines(false);
	}

}
