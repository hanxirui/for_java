package com.ez.framework.core.scheduler;


/**
 * @author liuyang
 *
 */
public enum CycleType {
	/**
	 * <code>None</code> - 无周期.
	 */
	None, 
	/**
	 * <code>Day</code> - 按小时执行周期.
	 */
	HOUR, 
	/**
	 * <code>Day</code> - 按天执行周期.
	 */
	Day, 
	/**
	 * <code>Week</code> - 安周执行周期.
	 */
	Week, 
	/**
	 * <code>Month</code> - 按月执行周期.
	 */
	Month, 
	/**
	 * <code>Year</code> - 按年执行周期.
	 */
	Year,
	/**
	 * <code>Year</code> - 始终.
	 */
	Cycle
}
