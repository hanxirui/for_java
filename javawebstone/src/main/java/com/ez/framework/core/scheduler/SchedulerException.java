package com.ez.framework.core.scheduler;


public class SchedulerException extends Exception {

	/**
	 * <code>serialVersionUID</code> - {description}.
	 */
	private static final long serialVersionUID = -1618749037213218599L;

	public SchedulerException(String message, Throwable t) {
		super(message, t);
	}
	public SchedulerException(String message){
		super(message);
	}
}
