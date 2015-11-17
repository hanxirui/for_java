package com.ez.framework.core.scheduler;

import java.io.Serializable;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import com.ez.framework.core.pojo.AbsPojo;


/**
 * @author liuyang
 *
 */
public class Scheduler extends AbsPojo implements Cloneable, Serializable {
	/**
	 * <code>logger</code> - logger.
	 */
	private static final Logger logger = Logger.getLogger(Scheduler.class);
	/**
	 * <code>NONE</code> - none.
	 */
	static final int NONE = -1;
	/**
	 * <code>serialVersionUID</code> - {description}.
	 */
	private static final long serialVersionUID = 4899305446343666841L;
	/**
	 * <code>startTime</code> - start time.
	 */
	private Date startTime;
	/**
	 * <code>endTime</code> - end time.
	 */
	private Date endTime;
	/**
	 * <code>intervalUnit</code> - interval unit.
	 */
	private IntervalUnit intervalUnit;
	/**
	 * <code>interval</code> - interval value.
	 */
	private int interval = NONE;
	/**
	 * <code>trigger</code> - trigger.
	 */
	private Trigger trigger;
	/**
	 * <code>jobDetail</code> - job detail.
	 */
	private JobDetail jobDetail;
	/**
	 * <code>jobClass</code> - job class.
	 */
	private Class<? extends AbstractJob> jobClass;
	/**
	 * <code>cycleType</code> - cycle type.
	 */
	private CycleType cycleType = CycleType.Day;
	/**
	 * <code>second</code> - second.
	 */
	private int second = NONE;
	/**
	 * <code>minute</code> - minute.
	 */
	private int minute = NONE;
	/**
	 * <code>hour</code> - hour.
	 */
	private int hour = NONE;
	/**
	 * <code>dayOfMonth</code> - day of month.
	 */
	private int dayOfMonth = NONE;
	/**
	 * <code>lastDayOfMonth</code> - is last day of month.
	 */
	private boolean lastDayOfMonth = Boolean.FALSE;
	/**
	 * <code>weeks</code> - weeks.
	 */
	private Week[] weeks;
	/**
	 * <code>months</code> - months.
	 */
	private Month[] months;
	/**
	 * <code>cronExpression</code> - cron exress.
	 */
	private String cronExpression;
	/**
	 * <code>schedulerType</code> - schedule type.
	 */
	private SchedulerType schedulerType = SchedulerType.custom;
	/**
	 * <code>jobDataMap</code> - job data map.
	 */
	private JobDataMap jobDataMap;
	/**
	 * <code>executable</code> - can execute?.
	 */
	private boolean executable = Boolean.TRUE;
	/**
	 * <code>errorMessage</code> - error message.
	 */
	private String errorMessage;
	
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public IntervalUnit getIntervalUnit() {
		return intervalUnit;
	}

	public void setIntervalUnit(IntervalUnit intervalUnit) {
		this.intervalUnit = intervalUnit;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public Trigger getTrigger() {
		return trigger;
	}

	public void setTrigger(Trigger trigger) {
		this.trigger = trigger;
	}

	public Class<? extends AbstractJob> getJobClass() {
		return jobClass;
	}

	public void setJobClass(Class<? extends AbstractJob> jobClass) {
		this.jobClass = jobClass;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getDayOfMonth() {
		return dayOfMonth;
	}

	public void setDayOfMonth(int dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	public boolean isLastDayOfMonth() {
		return lastDayOfMonth;
	}

	public void setLastDayOfMonth(boolean lastDayOfMonth) {
		this.lastDayOfMonth = lastDayOfMonth;
	}

	public Week[] getWeeks() {
		return weeks;
	}

	public long getWeekOpts() {
		return Week.getOpts(weeks);
	}

	public void setWeeks(Week[] weeks) {
		this.weeks = weeks;
	}

	public void setWeekOpts(long opts) {
		this.weeks = Week.getWeeks(opts);
	}

	public Month[] getMonths() {
		return months;
	}

	public long getMonthOpts() {
		return Month.getOpts(months);
	}

	public void setMonths(Month[] months) {
		this.months = months;
	}

	public void setMonthOpts(long opts) {
		this.months = Month.getMonths(opts);
	}

	public CycleType getCycleType() {
		return cycleType;
	}

	public void setCycleType(CycleType cycleType) {
		this.cycleType = cycleType;
	}

	public JobDetail getJobDetail() {
		return jobDetail;
	}

	public void setJobDetail(JobDetail jobDetail) {
		this.jobDetail = jobDetail;
	}

	public void setJobClassName(String jobClassName){
		try {
			this.jobClass = SchedulerManager.findClass(jobClassName);
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public String getJobClassName() {
		return this.jobClass.getName();
	}

	public SchedulerType getSchedulerType() {
		return schedulerType;
	}

	public void setSchedulerType(SchedulerType schedulerType) {
		this.schedulerType = schedulerType;
	}

	public JobDataMap getJobDataMap() {
		return jobDataMap;
	}

	public void setJobDataMap(JobDataMap jobDataMap) {
		this.jobDataMap = jobDataMap;
	}


	public boolean isExecutable() {
		return executable;
	}

	public void setExecutable(boolean executable) {
		this.executable = executable;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	protected Scheduler clone() {
		try {
			return (Scheduler) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

}
