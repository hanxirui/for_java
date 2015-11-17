package com.ez.framework.core.scheduler;


/**
 * @author liuyang
 *
 */
interface DateUnit {
	/**
	 * 值.
	 * 
	 * @return
	 */
	int getValue();

	/**
	 * 操作符.
	 * 
	 * @return
	 */
	int getOpt();

	/**
	 * 通配符.
	 * 
	 * @return
	 */
	String getWildcard();
}
