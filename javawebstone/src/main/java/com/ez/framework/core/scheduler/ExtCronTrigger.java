package com.ez.framework.core.scheduler;

import java.util.Date;

import org.quartz.Calendar;
import org.quartz.CronTrigger;
import org.quartz.impl.triggers.CoreTrigger;
import org.quartz.impl.triggers.CronTriggerImpl;

/**
 * @author liuyang
 *
 */
public class ExtCronTrigger extends CronTriggerImpl implements CronTrigger, CoreTrigger {

	/**
	 * <code>serialVersionUID</code> - {description}.
	 */
	private static final long serialVersionUID = -1163249933588071754L;

	/**
	 * <code>firstFireTime</code> - first fire time.
	 */
	private Date firstFireTime;
	/**
	 * <code>intervalUnit</code> - interval unit.
	 */
	private IntervalUnit intervalUnit;
	/**
	 * <code>interval</code> - interval.
	 */
	private int interval;

	/**
	 * set first fire time.
	 * @param firstFireTime
	 */
	public void setFirstFireTime(Date firstFireTime) {
		this.firstFireTime = firstFireTime;
	}

	/**
	 * set interval unit.
	 * @param intervalUnit
	 */
	public void setIntervalUnit(IntervalUnit intervalUnit) {
		this.intervalUnit = intervalUnit;
	}

	/**
	 * set interval.
	 * @param interval
	 */
	public void setInterval(int interval) {
		this.interval = interval;
	}
	
	/**
	 * whether has interval.
	 * @return
	 */
	private boolean hasInterval() {
		return interval > 0 || intervalUnit != null;
	}
	
	/* (non-Javadoc)
	 * @see org.quartz.impl.triggers.CronTriggerImpl#getFireTimeAfter(java.util.Date)
	 */
	@Override
	public Date getFireTimeAfter(Date date) {
		Date nextDate = super.getFireTimeAfter(date);
		if (hasInterval()) {
			int _interval = 0;
			while (true) {
				_interval = intervalUnit.getInterval(firstFireTime, nextDate);
				if (_interval % interval == 0) {
					break;
				}
				nextDate = super.getFireTimeAfter(nextDate);
			}

		}
		return nextDate;
	}
	
	/* (non-Javadoc)
	 * @see org.quartz.impl.triggers.CronTriggerImpl#computeFirstFireTime(org.quartz.Calendar)
	 */
	@Override
	public Date computeFirstFireTime(Calendar calendar) {
		Date nextTime = super.computeFirstFireTime(calendar);
		long now = System.currentTimeMillis();
		while(nextTime.getTime() <= now) {
			nextTime = getFireTimeAfter(nextTime);
		}
		setNextFireTime(nextTime);
		return nextTime;
	}

}
