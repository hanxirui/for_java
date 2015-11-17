package com.ez.framework.core.dao;


public class DAOException extends Exception {

	/**
	 * <code>serialVersionUID</code> - {description}.
	 */
	private static final long serialVersionUID = 7212727779900640468L;

	public DAOException(){
		super();
	}
	public DAOException(final String message){
		super(message);
	}
	public DAOException(final Throwable cause){
		super(cause);
	}
	public DAOException(final String message,final Throwable cause){
		super(message,cause);
	}
}
