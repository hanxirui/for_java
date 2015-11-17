package com.ez.framework.core.service;

public class ServiceException extends Exception {

	/**
	 * <code>serialVersionUID</code> - {description}.
	 */
	private static final long serialVersionUID = -215691064532756353L;
	public ServiceException(){
		super();
	}
	public ServiceException(final String message){
		super(message);
	}
	public ServiceException(final String message,final Throwable cause){
		super(message,cause);
	}
	public ServiceException(final Throwable cause){
		super(cause);
	}
}
