package com.ez.framework.core.scheduler;

import java.text.ParseException;

import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.ScheduleBuilder;
import org.quartz.spi.MutableTrigger;

/**
 * @author liuyang
 *
 */
public class ExtQuartzCronSchedulerBuilder extends ScheduleBuilder<CronTrigger> {

	/**
	 * <code>cronExpression</code> - {description}.
	 */
	private CronExpression cronExpression;
	/**
	 * <code>misfireInstruction</code> - {description}.
	 */
	private int misfireInstruction = CronTrigger.MISFIRE_INSTRUCTION_SMART_POLICY;

	/**
	 * Constructors.
	 * @param cronExpression
	 */
	private ExtQuartzCronSchedulerBuilder(CronExpression cronExpression) {
		if (cronExpression == null) {
			throw new NullPointerException("cronExpression cannot be null");
		}
		this.cronExpression = cronExpression;
	}

	/* (non-Javadoc)
	 * @see org.quartz.ScheduleBuilder#build()
	 */
	@Override
	public MutableTrigger build() {
		ExtCronTrigger ct = new ExtCronTrigger();
		ct.setCronExpression(cronExpression);
		ct.setTimeZone(cronExpression.getTimeZone());
		ct.setMisfireInstruction(misfireInstruction);
		return ct;
	}

	/**
	 * {method description}.
	 * @param cronExpression
	 * @return
	 */
	public static ExtQuartzCronSchedulerBuilder cronSchedule(String cronExpression) {
		try {
			return cronSchedule(new CronExpression(cronExpression));
		} catch (ParseException e) {
			throw new RuntimeException("CronExpression '" + cronExpression + "' is invalid,.", e);
		}
	}

	/**
	 * {method description}.
	 * @param cronExpression
	 * @return
	 */
	public static ExtQuartzCronSchedulerBuilder cronSchedule(CronExpression cronExpression) {
		return new ExtQuartzCronSchedulerBuilder(cronExpression);
	}

}
