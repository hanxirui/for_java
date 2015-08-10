package com.hxr.javatone.concurrency.java7concurrency.chapter2.recipe5;

/**
 * This class implements a writer that establish the prices
 *
 */
public class Writer implements Runnable {

	/**
	 * Class that stores the prices
	 */
	private final PricesInfo pricesInfo;
	
	/**
	 * Constructor of the class
	 * @param pricesInfo object that stores the prices
	 */
	public Writer(final PricesInfo pricesInfo){
		this.pricesInfo=pricesInfo;
	}
	
	/**
	 * Core method of the writer. Establish the prices
	 */
	@Override
	public void run() {
		for (int i=0; i<3; i++) {
			System.out.printf("Writer: Attempt to modify the prices.\n");
			pricesInfo.setPrices(Math.random()*10, Math.random()*8);
			System.out.printf("Writer: Prices have been modified.\n");
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
