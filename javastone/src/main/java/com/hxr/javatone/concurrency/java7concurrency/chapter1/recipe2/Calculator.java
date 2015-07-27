package com.hxr.javatone.concurrency.java7concurrency.chapter1.recipe2;

/**
 * This class prints the multiplication table of a number
 *
 */
public class Calculator implements Runnable {

	/**
	 *  The number
	 */
	private final int number;
	
	/**
	 *  Constructor of the class
	 * @param number : The number
	 */
	public Calculator(final int number) {
		this.number=number;
	}
	
	/**
	 *  Method that do the calculations
	 */
	@Override
	public void run() {
		for (int i=1; i<=10; i++){
			System.out.printf("%s: %d * %d = %d\n",Thread.currentThread().getName(),number,i,i*number);
		}
	}

}
