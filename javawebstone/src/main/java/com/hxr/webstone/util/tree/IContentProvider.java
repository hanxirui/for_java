package com.hxr.webstone.util.tree;

import java.util.List;

import com.ez.framework.core.service.ServiceException;

/**
 * 构告树结构时内容提供者接口 <br>
 * <p>
 * Create on : 2013-1-9<br>
 * </p>
 * <p>
 * 
 * @author liuyang<br>
 * @version riil.webcommons v6.2.0
 *          <p>
 *          <br>
 *          <strong>Modify History:</strong><br>
 *          user modify_date modify_content<br>
 *          -------------------------------------------<br>
 *          <br>
 */
public interface IContentProvider {

	/**
	 * 获取当前元素下的子元素.
	 * 
	 * @param curElement
	 *            当前元素对象
	 * @param level
	 *            当前元素级别
	 * @param index
	 *            当前元素所在层级索引
	 * @return 子元素对象集合
	 * @throws ServiceException
	 *             抛出异常
	 */
	List<? extends Object> getChildren(Object curElement, final int level, final int index) throws ServiceException;

	/**
	 * 获取根元素.
	 * 
	 * @param treeData
	 *            树结构所需数据
	 * @param level
	 *            当前级别
	 * @param index
	 *            当前索引
	 * @return 根元素对象集合
	 */
	List<Object> getRootElement(List<? extends Object> treeData, final int level, final int index);

	/**
	 * 异步时需实现此方法,用于判断当前元素是否有子元素.
	 * 
	 * @param curElement
	 *            当前元素对象
	 * @param level
	 *            当前元素级别
	 * @param index
	 *            当前元素所在层级索引
	 * @return true/false
	 */
	boolean hasChild(Object curElement, final int level, final int index);

}
