package com.ez.framework.core.servlet;

public class InfoException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4250891464492706606L;
	public InfoException(){
		super();
	}
	public InfoException(final String message){
		super(message);
	}
	public InfoException(final String message,final Throwable cause){
		super(message,cause);
	}
	public InfoException(final Throwable cause){
		super(cause);
	}
}
