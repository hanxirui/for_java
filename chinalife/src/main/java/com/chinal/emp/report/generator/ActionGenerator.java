package com.chinal.emp.report.generator;

import com.chinal.emp.report.action.AbsAction;

/**
 * {class description} <br>
 * <p>
 * Create on : 2016年1月26日<br>
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

public class ActionGenerator {

	/**
	 * Constructors.
	 */
	private ActionGenerator() {

	}

	/**
	 * {method description}.
	 * 
	 * @param className
	 *            className
	 * @return AbsBizExecutor
	 * @throws Exception
	 *             Exception
	 */
	public static AbsAction generator(final String className) throws Exception {
		try {

			return (AbsAction) Class.forName(className).newInstance();
		} catch (InstantiationException t_e) {
			throw new Exception(t_e);
		} catch (IllegalAccessException t_e) {
			throw new Exception(t_e);
		} catch (ClassNotFoundException t_e) {
			throw new Exception(t_e);
		}
	}
}