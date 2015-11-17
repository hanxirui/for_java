package com.ez.framework.core.scheduler;

import java.text.MessageFormat;


/**
 * @author liuyang
 *
 */
public class CronExpressionBuilder {

	/**
	 * <code>expressionPattern</code> - 表达式模板.
	 */
	private static final String expressionPattern = "{0} {1} {2} {3} {4} {5} {6}";
	public static final String Hour = "Hour";
	public static final String Min = "Min";
	/**
	 * 创建一个cron表达式构建器实例.
	 * 
	 * @return
	 */
	public static CronExpressionBuilder newBuilder() {
		return new CronExpressionBuilder();
	}

	/**
	 * 构建一个cron表达式.
	 * 
	 * @param scheduler
	 * @return
	 */
	public String build(Scheduler scheduler) {
		String _sec = scheduler.getSecond() <= Scheduler.NONE ? "*" : Integer.toString(scheduler.getSecond());
		String _min = scheduler.getMinute() <= Scheduler.NONE ? "*" : Integer.toString(scheduler.getMinute());
		String _hour = scheduler.getHour() <= Scheduler.NONE ? "*" : Integer.toString(scheduler.getHour());
		String _day = scheduler.isLastDayOfMonth() ? "L" : scheduler.getDayOfMonth() <= Scheduler.NONE ? "*" : Integer
				.toString(scheduler.getDayOfMonth());
		String _month = scheduler.getMonths() == null ? "*" : transform(scheduler.getMonths());
		String _weeks = scheduler.getWeeks() == null ? "?".equals(_day) ? "*" : "?" : transform(scheduler.getWeeks());
		if(!"?".equals(_weeks)) {
			_day = "?";
		}
		if(!"*".equals(_hour) && "*".equals(_min)) {
			_min = "0";
		}
		if(!"*".equals(_min) && "*".equals(_sec)) {
			_sec = "0";
		}
		String expression = MessageFormat.format(expressionPattern, _sec, _min, _hour, _day, _month, _weeks, "*");
		scheduler.setCronExpression(expression);
		return expression;
	}
	
	/**
	 * 转换日期单位.
	 * 
	 * @param units
	 * @return
	 */
	private String transform(DateUnit[] units) {
		if (units.length == 1 && units[0].getValue() == -1) {
			return units[0].getWildcard();
		} else {
			StringBuilder sb = new StringBuilder();
			for (DateUnit unit : units) {
				sb.append(unit.getValue()).append(',');
			}
			return sb.toString();
		}
	}
}
