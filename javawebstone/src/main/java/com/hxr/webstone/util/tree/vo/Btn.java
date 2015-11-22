package com.hxr.webstone.util.tree.vo;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 树节点按钮 <br>
 * <p>
 * Create on : 2011-9-6<br>
 * </p>
 * <br>
 * 
 * @author fangzhixin<br>
 * @version riil_web_common v1.0
 *          <p>
 *          <br>
 *          <strong>Modify History:</strong><br>
 *          user modify_date modify_content<br>
 *          -------------------------------------------<br>
 *          <br>
 */
public final class Btn {
	/**
	 * Constructors.
	 */
	private Btn() {

	}

	/**
	 * 创建元素.
	 * 
	 * @param isLast
	 *            是否是最后一个节点
	 * @param isExpend
	 *            节点是否展开
	 * @param isLeaf
	 *            是否是叶子节点
	 * @return Element DOM div元素
	 */
	public static Element create(final boolean isLast, final boolean isExpend, final boolean isLeaf) {
		String t_cls = "";

		if (isLast && isExpend && !isLeaf) {
			t_cls = "hitarea collapsable-hitarea lastCollapsable-hitarea";
		} else if (isLast && !isExpend && !isLeaf) {
			t_cls = "hitarea expandable-hitarea lastExpandable-hitarea";
		} else if (!isLast && !isExpend && !isLeaf) {
			t_cls = "hitarea expandable-hitarea";
		} else if (!isLast && isExpend && !isLeaf) {
			t_cls = "hitarea  collapsable-hitarea";
		} else if (isLast && isLeaf) {
			t_cls = "last";
		}

		if (isLeaf) {
			t_cls += " hitarea placeholder";
		}

		Element t_div = DocumentHelper.createElement("div");
		t_div.addText("");
		// div.attributeValue("class", cls);
		t_div.addAttribute("class", t_cls);

		return t_div;
	}

}
