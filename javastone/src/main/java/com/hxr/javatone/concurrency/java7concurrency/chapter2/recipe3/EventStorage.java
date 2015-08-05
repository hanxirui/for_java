package com.hxr.javatone.concurrency.java7concurrency.chapter2.recipe3;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * This class implements an Event storage. Producers will storage
 * events in it and Consumers will process them. An event will
 * be a java.util.Date object
 *
 */
public class EventStorage {
   
	/**
	 * Maximum size of the storage
	 */
	private final int maxSize;
	/**
	 * Storage of events
	 */
	private final List<Date> storage;
	
	/**
	 * Constructor of the class. Initializes the attributes.
	 */
	public EventStorage(){
		maxSize=10;
		storage=new LinkedList<>();
	}
	
	/**
	 * This method creates and storage an event.
	 */
	public synchronized void set(){
			while (storage.size()==maxSize){
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			storage.add(new Date());
			System.out.println("Set: "+storage.size());
			notify();
	}
	
	/**
	 * This method delete the first event of the storage.
	 */
	public synchronized void get(){
			while (storage.size()==0){
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("Get: "+storage.size()+": "+((LinkedList<?>)storage).poll());
			notify();
	}
	
}
