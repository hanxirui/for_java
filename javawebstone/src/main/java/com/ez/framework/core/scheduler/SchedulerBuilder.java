package com.ez.framework.core.scheduler;

import java.util.Date;

import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;


/**
 * @author liuyang
 *
 */
public class SchedulerBuilder {

	/**
	 * <code>TRIGGER_PATTERN</code> - trigger name pattern.
	 */
	private static final Pattern TRIGGER_PATTERN = new Pattern("%s_trigger");
	/**
	 * <code>GROUP_PATTERN</code> - group name pattern.
	 */
	private static final Pattern GROUP_PATTERN = new Pattern("%s_group");
	/**
	 * <code>JOB_PATTERN</code> - job name pattern.
	 */
	private static final Pattern JOB_PATTERN = new Pattern("%s_job");
	/**
	 * <code>scheduler</code> - scheduler instance.
	 */
	private Scheduler scheduler;
	
	/**
	 * name pattern
	 * <br>
	 *  
	 * <p>
	 * Create on : 2013-4-2<br>
	 * </p>
	 * <br>
	 * @author zhaoyang<br>
	 * @version riil.core v6.2.0
	 * <br>
	 * <strong>Modify History:</strong><br>
	 * user     modify_date    modify_content<br>
	 * -------------------------------------------<br>
	 * <br>
	 */
	private static class Pattern {
		/**
		 * <code>pattern</code> - pattern.
		 */
		private String pattern;
		/**
		 * Constructors.
		 * @param pattern
		 */
		public Pattern(String pattern) {
			this.pattern = pattern;
		}
		/**
		 * format name.
		 * @param content
		 * @return
		 */
		public String format(String content) {
			return String.format(pattern, content);
		}
	}

	/**
	 * rebuild a scheduler instance.
	 * @param scheduler
	 * @return
	 */
	static Scheduler rebuild(Scheduler scheduler) {
		SchedulerBuilder builder = new SchedulerBuilder(scheduler);
		return builder.build();
	}
	
	/**
	 * generate a job key.
	 * @param schedulerId
	 * @return
	 */
	static JobKey generateJobKey(String schedulerId) {
		return new JobKey(SchedulerBuilder.JOB_PATTERN.format(schedulerId), SchedulerBuilder.GROUP_PATTERN.format(schedulerId));
	}

	/**
	 * construct a scheduler builder instance.
	 * @return
	 */
	public static SchedulerBuilder newScheduler() {
		return new SchedulerBuilder();
	}

	/**
	 * Constructors.
	 * @param scheduler
	 */
	private SchedulerBuilder(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	/**
	 * Constructors.
	 */
	private SchedulerBuilder() {
		scheduler = new Scheduler();
	}

	/**
	 * 设置schedule id.
	 * @param schedulerId
	 * @return
	 */
	public SchedulerBuilder setId(String schedulerId) {
		scheduler.setId(schedulerId);
		return this;
	}

	/**
	 * 设置schedule的执行间隔.
	 * @param interval
	 * @param intervalUnit
	 * @return
	 */
	public SchedulerBuilder setInterval(int interval, IntervalUnit intervalUnit) {
		scheduler.setInterval(interval);
		scheduler.setIntervalUnit(intervalUnit);
		return this;
	}

	/**
	 * 设置schedule的执行开始时间.
	 * @param startTime
	 * @return
	 */
	public SchedulerBuilder setStartTime(Date startTime) {
		scheduler.setStartTime(startTime);
		return this;
	}

	/**
	 * 设置schedu的执行结束时间.
	 * @param endTime
	 * @return
	 */
	public SchedulerBuilder setEndTime(Date endTime) {
		scheduler.setEndTime(endTime);
		return this;
	}

	/**
	 * 设置schedule执行时间的秒钟.
	 * @param sec
	 * @return
	 */
	public SchedulerBuilder setSecond(int sec) {
		scheduler.setSecond(sec);
		return this;
	}

	/**
	 * 设置schedule执行时间的分钟.
	 * @param min
	 * @return
	 */
	public SchedulerBuilder setMinute(int min) {
		scheduler.setMinute(min);
		return this;
	}

	/**
	 * 设置schedule执行时间的时钟.
	 * @param hour
	 * @return
	 */
	public SchedulerBuilder setHour(int hour) {
		scheduler.setHour(hour);
		return this;
	}

	/**
	 * 设置schedule执行时间的天.
	 * @param dayOfMonth
	 * @return
	 */
	public SchedulerBuilder setDayOfMonth(int dayOfMonth) {
		if (dayOfMonth > 0) {
			scheduler.setDayOfMonth(dayOfMonth);
		}
		return this;
	}

	/**
	 * 设置schedule执行时间是否是当月中的最后一天.
	 * @param lastDayOfMonth
	 * @return
	 */
	public SchedulerBuilder setLastDayOfMonth(boolean lastDayOfMonth) {
		scheduler.setLastDayOfMonth(lastDayOfMonth);
		return this;
	}

	/**
	 * 设置schedule执行时间的月.
	 * @param months
	 * @return
	 */
	public SchedulerBuilder setMonths(Month[] months) {
		scheduler.setMonths(months);
		return this;
	}

	/**
	 * 设置schedule执行时间的周.
	 * @param weeks
	 * @return
	 */
	public SchedulerBuilder setWeeks(Week[] weeks) {
		scheduler.setWeeks(weeks);
		return this;
	}

	/**
	 * 设置cron表达式.
	 * @param cronExpression
	 * @return
	 */
	public SchedulerBuilder setCronExpression(String cronExpression) {
		scheduler.setCronExpression(cronExpression);
		return this;
	}

	/**
	 * 设置执行job的class.
	 * @param jobClass
	 * @return
	 */
	public SchedulerBuilder setJobClass(Class<? extends AbstractJob> jobClass) {
		scheduler.setJobClass(jobClass);
		return this;
	}
	
	/**
	 * 设置执行job的class name.
	 * @param className
	 * @return
	 */
	public SchedulerBuilder setJobClassName(String className) {
		scheduler.setJobClassName(className);
		return this;
	}

	/**
	 * 设置schedule执行周期类型.
	 * @param cycleType
	 * @return
	 */
	public SchedulerBuilder setCycleType(CycleType cycleType) {
		scheduler.setCycleType(cycleType);
		return this;
	}

	/**
	 * 设置schedule的类型.
	 * @param schedulerType
	 * @return
	 */
	protected SchedulerBuilder setSchedulerType(SchedulerType schedulerType) {
		scheduler.setSchedulerType(schedulerType);
		return this;
	}

	/**
	 * 设置schedule的参数.
	 * @param jobDataMap
	 * @return
	 */
	public SchedulerBuilder setJobDataMap(JobDataMap jobDataMap) {
		scheduler.setJobDataMap(jobDataMap);
		return this;
	}

	/**
	 * 构建schedule.
	 * @return
	 */
	public Scheduler build() {
		if (CycleType.None == scheduler.getCycleType()) {
			buildSimpleTrigger();
		} else {
			if (CycleType.Cycle == scheduler.getCycleType()){
				buildCycleSimpleTrigger(scheduler);
			}else{
				if (scheduler.getCronExpression() == null) {
					CronExpressionBuilder.newBuilder().build(scheduler);
				}
				buildCronTrigger();
			}
		}
		buildJobDetail();
		return scheduler;
	}

	/**
	 * 设置schedule的job.
	 */
	private void buildJobDetail() {
		JobBuilder jobBuilder = JobBuilder.newJob(scheduler.getJobClass())
				.withIdentity(JOB_PATTERN.format(scheduler.getId()), GROUP_PATTERN.format(scheduler.getId()));
		if (scheduler.getJobDataMap() != null) {
			jobBuilder = jobBuilder.usingJobData(scheduler.getJobDataMap());
		}
		scheduler.setJobDetail(jobBuilder.build());
	}

	/**
	 * 构建简单Trigger.
	 * 执行一次的Trigger。
	 */
	private void buildSimpleTrigger() {
		if(!verifyStartTime()) {
			return;
		}
		TriggerBuilder<SimpleTrigger> builder = TriggerBuilder
				.newTrigger()
				.withIdentity(TRIGGER_PATTERN.format(scheduler.getId()), GROUP_PATTERN.format(scheduler.getId()))
				.withSchedule(SimpleScheduleBuilder.simpleSchedule());
		builder = scheduler.getStartTime() == null ? builder.startNow() : builder.startAt(scheduler.getStartTime());
		if (scheduler.getEndTime() != null)
			builder = builder.endAt(scheduler.getEndTime());
		scheduler.setTrigger(builder.build());
	}

	/**
	 * 构建简单Trigger.
	 * 执行一次的Trigger。
	 */
	private void buildCycleSimpleTrigger(Scheduler scheduler) {
//		if(!verifyStartTime()) {
//			return;
//		}
		TriggerBuilder<SimpleTrigger> builder = TriggerBuilder
				.newTrigger()
				.withIdentity(TRIGGER_PATTERN.format(scheduler.getId()), GROUP_PATTERN.format(scheduler.getId()))
				.withSchedule(SimpleScheduleBuilder.simpleSchedule());
		builder = scheduler.getStartTime() == null ? builder.startNow() : builder.startAt(scheduler.getStartTime());
		int hour = scheduler.getHour();
		if(hour==0){
			int min = scheduler.getMinute();
			int repeatCount = 5256000/min;
			builder.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(min).withRepeatCount(repeatCount));
		}else{
			int repeatCount = 87600/hour;
			builder.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(hour).withRepeatCount(repeatCount));
		}
		scheduler.setTrigger(builder.build());
	}
	
	/**
	 * 验证开始时间.
	 * 如果早于当前时间则抛出{@link RuntimeException}
	 */
	private boolean verifyStartTime() {
		if(scheduler.getStartTime() != null && scheduler.getStartTime().before(new Date())) {
			scheduler.setExecutable(Boolean.FALSE);
			scheduler.setErrorMessage(String.format("the given scheduler will never fire, id is %s, start time is %s"
					,scheduler.getId(), scheduler.getStartTime().toString()));
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	
	/**
	 * 构建周期性Trigger.
	 */
	private void buildCronTrigger() {
		TriggerBuilder<CronTrigger> builder = TriggerBuilder
				.newTrigger()
				.withIdentity(TRIGGER_PATTERN.format(scheduler.getId()), GROUP_PATTERN.format(scheduler.getId()))
				.withSchedule(ExtQuartzCronSchedulerBuilder.cronSchedule(scheduler.getCronExpression()));
		Date startTime = scheduler.getStartTime() == null ? new Date() : scheduler.getStartTime();
		builder = builder.startAt(startTime);
		if (scheduler.getEndTime() != null)
			builder = builder.endAt(scheduler.getEndTime());
		ExtCronTrigger trigger = (ExtCronTrigger) builder.build();
		trigger.setFirstFireTime(startTime);
		trigger.setInterval(scheduler.getInterval());
		trigger.setIntervalUnit(scheduler.getIntervalUnit());
		scheduler.setTrigger(trigger);
	}
}
