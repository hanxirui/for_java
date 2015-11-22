package com.hxr.webstone.util.tree;

import java.util.Map;

/**
 * 构造树结构时节点显示内容提供者接口 <br>
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
public interface ILabelProvider {

	/**
	 * 获得当前元素类型Normal,Checkbox,Radio.
	 * 
	 * @param curElement
	 *            当前元素对象
	 * @param level
	 *            级别
	 * @param index
	 *            索引
	 * @return TreeTypeEnum枚举
	 */
	TreeTypeEnum getLabelType(final Object curElement, final int level, final int index);

	/**
	 * 获取当前元素显示名称.
	 * 
	 * @param curElement
	 *            当前元素对象
	 * @param level
	 *            级别
	 * @param index
	 *            索引
	 * @return 节点名称
	 */
	String getLabelName(final Object curElement, final int level, final int index);

	/**
	 * 获得当前元素的ID.
	 * 
	 * @param curElement
	 *            当前元素
	 * @param level
	 *            级别
	 * @param index
	 *            索引
	 * @return 节点ID
	 */
	String getLabelId(final Object curElement, final int level, final int index);

	/**
	 * 获取当前元素显示图标.如果不显示图标，返回null
	 * 
	 * @param curElement
	 *            当前元素
	 * @param level
	 *            级别
	 * @param index
	 *            索引
	 * @return 节 点图标名称
	 */
	String getIcon(final Object curElement, final int level, final int index);

	/**
	 * 获取当前元素是否显示默认图标.
	 * 
	 * @param curElement
	 *            当前元素
	 * @param level
	 *            级别
	 * @param index
	 *            索引
	 * @return true/false
	 */
	boolean isDefaultIcon(final Object curElement, final int level, final int index);

	/**
	 * 获取当前元素是否有扩展操作.
	 * 
	 * @param curElement
	 *            当前元素
	 * @param level
	 *            级别
	 * @param index
	 *            索引
	 * @return true/false
	 */
	boolean isExtOpra(final Object curElement, int level, int index);

	/**
	 * 获取当前元素是否可以点击操作
	 * 
	 * @param curElement
	 *            当前元素
	 * @param level
	 *            级别
	 * @param index
	 *            索引
	 * @return true/false
	 */
	boolean isClick(final Object curElement, int level, int index);

	/**
	 * 如是单选或多选情况下会调用该方法，判断当前节点是否被选中.
	 * 
	 * @param curElement
	 *            当前元素
	 * @param level
	 *            级别
	 * @param index
	 *            索引
	 * @return true/false
	 */
	boolean isCheck(final Object curElement, int level, int index);

	/**
	 * 如是单选或多选情况下会调用该方法，获取节点选中时设置的值.
	 * 
	 * @param curElement
	 *            当前元素
	 * @param level
	 *            级别
	 * @param index
	 *            索引
	 * @return 节点值
	 */
	String getCheckId(final Object curElement, int level, int index);

	/**
	 * 获得当前元素的自定义属性.
	 * 
	 * @param curElement
	 *            当前元素
	 * @param level
	 *            级别
	 * @param index
	 *            索引
	 * @return 自定义属性MAP
	 */
	Map<String, String> getAttributes(final Object curElement, int level, int index);

}
