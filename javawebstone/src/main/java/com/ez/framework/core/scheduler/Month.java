package com.ez.framework.core.scheduler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * @author liuyang
 *
 */
public enum Month implements DateUnit {
	ALL(-1, 1 << 0), JANUARY(Calendar.JANUARY + 1, 1 << 1), FEBRUARY(Calendar.FEBRUARY + 1, 1 << 2), MARCH(Calendar.MARCH + 1,
			1 << 3), APRIL(Calendar.APRIL + 1, 1 << 4), MAY(Calendar.MAY + 1, 1 << 5), JUNE(Calendar.JUNE + 1, 1 << 6), JULY(
			Calendar.JULY + 1, 1 << 7), AUGUST(Calendar.AUGUST + 1, 1 << 8), SEPTEMBER(Calendar.SEPTEMBER + 1, 1 << 9), OCTOBER(
			Calendar.OCTOBER + 1, 1 << 10), NOVEMBER(Calendar.NOVEMBER + 1, 1 << 11), DECEMBER(Calendar.DECEMBER + 1, 1 << 12);

	private Month(int value, int opt) {
		this.value = value;
		this.opt = opt;
	}

	private int value;
	private int opt;

	public int getValue() {
		return value;
	}

	public String getWildcard() {
		return "*";
	}

	public int getOpt() {
		return opt;
	}

	public static Month[] getMonths(long opts) {
		List<Month> list = new ArrayList<Month>();
		for (Month month : Month.values()) {
			if ((opts & month.getOpt()) > 0) {
				list.add(month);
			}
		}
		return list.toArray(new Month[list.size()]);
	}

	public static long getOpts(Month[] months) {
		long opts = 0L;
		if (months != null) {
			for (Month month : months) {
				opts = opts | month.getOpt();
			}
		}
		return opts;
	}
}
