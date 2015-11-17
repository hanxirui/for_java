package com.ez.framework.core.scheduler;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author liuyang
 *
 */
public enum IntervalUnit {
	/**
	 * <code>Min</code> - 分钟间隔.
	 */
	Min{
		@Override
		public int getInterval(Date left, Date right) {
			return (int) ((left.getTime() - right.getTime()) / oneMinTime);
		}
	},/**
	 * <code>Hour</code> - 小时间隔.
	 */
	Hour{
		@Override
		public int getInterval(Date left, Date right) {
			return (int) ((left.getTime() - right.getTime()) / oneHourTime);
		}
	},/**
	 * <code>Hour</code> - 小时间隔.
	 */
	HalfDay{
		@Override
		public int getInterval(Date left, Date right) {
			return (int) ((left.getTime() - right.getTime()) / (12*oneHourTime));
		}
	},
	/**
	 * <code>Day</code> - 天间隔.
	 */
	Day{
		@Override
		public int getInterval(Date left, Date right) {
			return (int) ((left.getTime() - right.getTime()) / oneDayTime);
		}
	},

	/**
	 * <code>Week</code> - 周间隔.
	 */
	Week{
		@Override
		public int getInterval(Date left, Date right) {
			leftCal.setTime(left);
			rightCal.setTime(right);
			leftCal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			rightCal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			return (int) ((rightCal.getTimeInMillis() - leftCal.getTimeInMillis()) / oneWeekTime);
		}

	},

	/**
	 * <code>Month</code> - 月间隔.
	 */
	Month{
		@Override
		public int getInterval(Date left, Date right) {
			int days = 0;
			leftCal.setTime(left);
			rightCal.setTime(right);
			days = (rightCal.get(Calendar.YEAR) - leftCal.get(Calendar.YEAR)) * 12
					+ (rightCal.get(Calendar.MONTH) - leftCal.get(Calendar.MONTH));
			return days;
		}

	};

	/**
	 * <code>oneMinTime</code> - {description}.
	 */
	private static final long oneMinTime = TimeUnit.MINUTES.toMillis(1);
	/**
	 * <code>oneHourTime</code> - {description}.
	 */
	private static final long oneHourTime = TimeUnit.HOURS.toMillis(1);
	/**
	 * <code>oneDayTime</code> - {description}.
	 */
	private static final long oneDayTime = TimeUnit.DAYS.toMillis(1);

	/**
	 * <code>oneWeekTime</code> - {description}.
	 */
	private static final long oneWeekTime = TimeUnit.DAYS.toMillis(7);

	/**
	 * <code>leftCal</code> - {description}.
	 */
	private static final Calendar leftCal = Calendar.getInstance();

	/**
	 * <code>rightCal</code> - {description}.
	 */
	private static final Calendar rightCal = Calendar.getInstance();

	/**
	 * 取得两个日期之间的间隔数.
	 * @param left
	 * @param right
	 * @return
	 */
	public int getInterval(Date left, Date right) {
		throw new AbstractMethodError();
	}
}
