package com.hxr.javatone.concurrency.java7concurrency.chapter2.recipe3;

/**
 * This class implements a consumer of events.
 *
 */
public class Consumer implements Runnable {

	/**
	 * Store to work with
	 */
	private final EventStorage storage;
	
	/**
	 * Constructor of the class. Initialize the storage
	 * @param storage The store to work with
	 */
	public Consumer(final EventStorage storage){
		this.storage=storage;
	}
	
	/**
	 * Core method for the consumer. Consume 100 events
	 */
	@Override
	public void run() {
		for (int i=0; i<100; i++){
			storage.get();
		}
	}

}
