package com.chinal.emp.report.util;

import java.math.BigDecimal;

/**
 * Apdex总体验指数算法. <br>
 * Apdex=(满意样本+容忍样本/2)/样本总数
 * <p>
 * Create on : 2016年2月25日<br>
 * </p>
 * <br>
 * 
 * @author chengxuetao@ruijie.com.cn<br>
 * @version riil-education-action v6.7.8 <br>
 *          <strong>Modify History:</strong><br>
 *          user modify_date modify_content<br>
 *          -------------------------------------------<br>
 *          <br>
 */
public class ApdexUtil {

	/**
	 * S_100
	 */
	private static final int S_100 = 100;

	/**
	 * Constructors.
	 */
	private ApdexUtil() {
	}

	/**
	 * 计算总体验指数.
	 * 
	 * @param total
	 *            样本总数
	 * @param satisfaction
	 *            满意样本数量
	 * @param tolerate
	 *            容忍样本数量
	 * @return 总体验指数
	 */
	public static String calculate(final int total, final int satisfaction, final int tolerate) {
		if (total == 0) {
			return "";
		}
		if (total == satisfaction) {
			return String.valueOf(S_100);
		}
		float apdex = (satisfaction + (float) tolerate / 2) / total;
		apdex = apdex * S_100;
		BigDecimal b = new BigDecimal(String.valueOf(apdex));
		return b.setScale(0, BigDecimal.ROUND_FLOOR).toString();
	}

}
