package com.hxr.javatone.concurrency.java7concurrency.chapter2.recipe1.solution;

/**
 * This class simulates a company that pays a salary an
 * insert money into an account 
 *
 */
public class Company implements Runnable {
	/**
	 * The account affected by the operations
	 */
	private final Account account;
	
	/**
	 * Constructor of the class. Initializes the account
	 * @param account the account affected by the operations
	 */
	public Company(final Account account) {
		this.account=account;
	}
	
	/**
	 * Core method of the Runnable
	 */
	@Override
    public void run() {
		for (int i=0; i<100; i++){
			account.addAmount(1000);
		}
	}

}
