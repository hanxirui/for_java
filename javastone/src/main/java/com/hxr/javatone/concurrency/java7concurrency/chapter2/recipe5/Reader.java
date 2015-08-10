package com.hxr.javatone.concurrency.java7concurrency.chapter2.recipe5;

/**
 * This class implements a reader that consults the prices
 *
 */
public class Reader implements Runnable {

	/**
	 * Class that stores the prices
	 */
	private final PricesInfo pricesInfo;
	
	/**
	 * Constructor of the class
	 * @param pricesInfo object that stores the prices
	 */
	public Reader (final PricesInfo pricesInfo){
		this.pricesInfo=pricesInfo;
	}
	
	/**
	 * Core method of the reader. Consults the two prices and prints them
	 * to the console
	 */
	@Override
	public void run() {
		for (int i=0; i<10; i++){
			System.out.printf("%s: Price 1: %f\n",Thread.currentThread().getName(),pricesInfo.getPrice1());
			System.out.printf("%s: Price 2: %f\n",Thread.currentThread().getName(),pricesInfo.getPrice2());
		}
	}

}
