package com.ez.framework.core.scheduler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * @author liuyang
 *
 */
public enum Week implements DateUnit {
	ALL(-1, 1 << 0), SUNDAY(Calendar.SUNDAY, 1 << 1), MONDAY(Calendar.MONDAY, 1 << 2), TUESDAY(Calendar.TUESDAY, 1 << 3), WEDNESDAY(
			Calendar.WEDNESDAY, 1 << 4), THURSDAY(Calendar.THURSDAY, 1 << 5), FRIDAY(Calendar.FRIDAY, 1 << 6), SATURDAY(
			Calendar.SATURDAY, 1 << 7);

	private Week(int value, int opt) {
		this.value = value;
		this.opt = opt;
	}

	private int value;
	private int opt;

	public int getValue() {
		return value;
	}

	public String getWildcard() {
		return "?";
	}

	public int getOpt() {
		return opt;
	}
	
	public static Week parse(int value) {
		for (Week week : Week.values()) {
			if(week.getValue() == value) {
				return week;
			}
		}
		return null;
	}

	public static Week[] getWeeks(long opts) {
		List<Week> list = new ArrayList<Week>();
		for (Week week : Week.values()) {
			if ((opts & week.getOpt()) > 0) {
				list.add(week);
			}
		}
		return list.toArray(new Week[list.size()]);
	}

	public static long getOpts(Week[] weeks) {
		long opts = 0L;
		if (weeks != null) {
			for (Week week : weeks) {
				opts = opts | week.getOpt();
			}
		}
		return opts;
	}
}
