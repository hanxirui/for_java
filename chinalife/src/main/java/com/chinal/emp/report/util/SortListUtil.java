package com.chinal.emp.report.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 排序工具类<br>
 * <p>
 * Create on : 2012-5-8<br>
 * <p>
 * </p>
 * <br>
 * 
 * @author liuchao@ruijie.com.cn<br>
 * @version riil.webcommons v6.0
 *          <p>
 *          <br>
 *          <strong>Modify History:</strong><br>
 *          user modify_date modify_content<br>
 *          -------------------------------------------<br>
 *          <br>
 * @param <E>
 */

public class SortListUtil<E> {

	/**
	 * 排序方法
	 * 
	 * @param list
	 *            待排序集合
	 * @param method
	 *            类的方法名
	 * @param sort
	 *            排序方式（asc/desc）
	 */
	public void sort(final List<E> list, final String method, final String sort) {
		Collections.sort(list, new Comparator<Object>() {
			@SuppressWarnings("unchecked")
			public int compare(final Object a, final Object b) {
				int t_ret = 0;
				try {
					Method t_m1 = ((E) a).getClass().getMethod(method, null);
					Method t_m2 = ((E) b).getClass().getMethod(method, null);
					String t_c1 = "-";
					String t_c2 = "-";
					if (sort != null && "desc".equals(sort)) {

						if (t_m1.invoke(((E) a), null) != null) {
							t_c1 = t_m1.invoke(((E) a), null).toString();
						}

						if (t_m2.invoke(((E) b), null) != null) {
							t_c2 = t_m2.invoke(((E) b), null).toString();
						}

						t_ret = t_c2.compareTo(t_c1);

					} else {
						if (t_m1.invoke(((E) a), null) != null) {
							t_c1 = t_m1.invoke(((E) a), null).toString();
						}

						if (t_m2.invoke(((E) b), null) != null) {
							t_c2 = t_m2.invoke(((E) b), null).toString();
						}

						t_ret = t_c1.compareTo(t_c2);

					}
				} catch (NoSuchMethodException t_e) {
					t_e.printStackTrace();
				} catch (IllegalAccessException t_e) {
					t_e.printStackTrace();
				} catch (InvocationTargetException t_e) {
					t_e.printStackTrace();
				}

				return t_ret;
			}
		});
	}

}
