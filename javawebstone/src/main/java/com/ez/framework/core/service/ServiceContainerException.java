package com.ez.framework.core.service;


public class ServiceContainerException extends Exception {

	/**
	 * <code>serialVersionUID</code> - {Field description}.
	 */
	private static final long serialVersionUID = -8183653717768610066L;

	/**
	 * Constructors.
	 */
	public ServiceContainerException() {
		super();
	}
	public ServiceContainerException(String message){
		super(message);
	}
	/**
	 * Constructors.
	 * 
	 * @param cause
	 */
	public ServiceContainerException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructors.
	 * 
	 * @param message
	 * @param cause
	 */
	public ServiceContainerException(String message, Throwable cause) {
		super(message, cause);
	}
}
