package com.hxr.webstone.util.tree;

/**
 * 构造树扩展接口. <br>
 * 
 * <p>
 * Create on : Aug 21, 2013<br>
 * </p>
 * <br>
 * 
 * @author weiyi<br>
 * @version riil-bmc-framework v6.2.0 <br>
 *          <strong>Modify History:</strong><br>
 *          user modify_date modify_content<br>
 *          -------------------------------------------<br>
 *          <br>
 */
public interface IExtendProvider {
	/**
	 * 获取扩展工具样式.
	 * 
	 * @param index
	 *            index
	 * @param level
	 *            level
	 * @param object
	 *            tree data
	 * @return 获取扩展工具样式.
	 */
	String getExtendToolStyle(Object object, int level, int index);
}
