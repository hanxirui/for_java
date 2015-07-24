package com.hxr.javatone.concurrency.java7concurrency.chapter5.recipe1;

import java.util.List;
import java.util.concurrent.RecursiveAction;

/**
 * This class implements the tasks that are going to update the
 * price information. If the assigned interval of values is less that 10, it
 * increases the prices of the assigned products. In other case, it divides
 * the assigned interval in two, creates two new tasks and execute them
 *
 */
public class Task extends RecursiveAction {

	/**
	 * serial version UID. The ForkJoinTask class implements the serializable interface.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * List of products
	 */
	private final List<Product> products;
	
	/**
	 * Fist and Last position of the interval assigned to the task
	 */
	private final int first;
	private final int last;
	
	/**
	 * Increment in the price of products this task has to apply
	 */
	private final double increment;
	
	/**
	 * Constructor of the class. Initializes its attributes
	 * @param products list of products
	 * @param first first element of the list assigned to the task
	 * @param last last element of the list assigned to the task
	 * @param increment price increment that this task has to apply
	 */
	public Task (final List<Product> products, final int first, final int last, final double increment) {
		this.products=products;
		this.first=first;
		this.last=last;
		this.increment=increment;
	}
	
	/**
	 * Method that implements the job of the task
	 */
	@Override
	protected void compute() {
		if (last-first<10) {
			updatePrices();
		} else {
			int middle=(last+first)/2;
			System.out.printf("Task: Pending tasks: %s\n",getQueuedTaskCount());
			Task t1=new Task(products, first,middle+1, increment);
			Task t2=new Task(products, middle+1,last, increment);
			invokeAll(t1, t2);	
		}
	}

	/**
	 * Method that updates the prices of the assigned products to the task
	 */
	private void updatePrices() {
		for (int i=first; i<last; i++){
			Product product=products.get(i);
			product.setPrice(product.getPrice()*(1+increment));
		}
	}

}
