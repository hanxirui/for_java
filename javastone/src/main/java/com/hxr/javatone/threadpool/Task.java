package com.hxr.javatone.threadpool;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: use to test thread pool
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author xingyong
 * 
 * @version 1.0
 */

public interface Task {
	/**
	 * set status of task .
	 * 
	 * @param flag
	 */
	public void setEnd(boolean flag);

	/**
	 * start task
	 * 
	 * @throws java.lang.Exception
	 */
	public abstract void startTask() throws Exception;

	/**
	 * end tast
	 * 
	 * @throws java.lang.Exception
	 */
	public abstract void endTask() throws Exception;

	/**
	 * get status of task
	 * 
	 * @return boolean
	 */
	public boolean isEnd();

}
