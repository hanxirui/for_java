package com.hxr.webstone.util.tree.vo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 树节点类 <br>
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
/**
 * {class description} <br>
 * <p>
 * Create on : 2011-11-7<br>
 * </p>
 * <br>
 * 
 * @author fangzhixin<br>
 * @version riil.web.common v1.0
 *          <p>
 *          <br>
 *          <strong>Modify History:</strong><br>
 *          user modify_date modify_content<br>
 *          -------------------------------------------<br>
 *          <br>
 */
public class TreeNode {

	/**
	 * <code>COLLAPSABLE</code> - 展开样式名.
	 */
	private static String s_COLLAPSABLE = "collapsable";
	/**
	 * <code>EXPANDABLE</code> - 折叠样式名.
	 */
	private static String s_EXPANDABLE = "expandable";
	/**
	 * <code>LAST</code> - 同级最后一个节点样式名.
	 */
	private static String s_LAST = "last";
	/**
	 * <code>LAST_COLLAPSABLE</code> - 同级最后一个节点且展开样式名.
	 */
	private static String s_LASTCOLLAPSABLE = s_LAST + "Collapsable";
	/**
	 * <code>LAST_EXPANDABLE</code> - 同级最后一个节点且折叠样式名.
	 */
	private static String s_LASTEXPANDABLE = s_LAST + "Expandable";

	/**
	 * <code>EXPENDCLS</code> - 该节点展开，子结点ul，显示样式.
	 */
	private static String s_EXPENDCLS = "display:block";
	/**
	 * <code>COLLAPCLS</code> -该节点折叠，子结点ul，显示样式.
	 */
	private static String s_COLLAPCLS = "display:none";

	/**
	 * <code>LAST_EXPEND_ROOT</code> - 如果是最后一个，并且是展开的,不是叶子节点 collapsable 拼装后为
	 * lastCollapsable.
	 */
	private static String s_LASTEXPENDROOT = s_COLLAPSABLE + " " + s_LASTCOLLAPSABLE;
	/**
	 * <code>LAST_COLLAPS_ROOT</code> - 如果是最后一个，并且是关闭的,不是叶子节点 expandable
	 * 拼装后为lastExpandable.
	 */
	private static String s_LASTCOLLAPSROOT = s_EXPANDABLE + " " + s_LASTEXPANDABLE;

	/**
	 * <code>isExpand</code> - {description}.
	 */
	private boolean m_isExpand = false;
	/**
	 * <code>isLeaf</code> -该节点是否为叶节点，即没有子结点.
	 */
	private Boolean m_isLeaf = null;

	/**
	 * <code>node</code> - 节点DOM元素对象.
	 */
	private Element m_node = null;

	/**
	 * <code>component</code> - 树节点组件（折叠按钮/图标/单复选框/显示文本/操作按钮）.
	 */
	private final List<Element> m_component = new ArrayList<Element>();

	/**
	 * <code>childs</code> - 该节点下的子节点.
	 */
	private List<TreeNode> m_childs = null;

	/**
	 * <code>id</code> - 节点ID.
	 */
	private String m_id = null;

	/**
	 * <code>m_style</code> - 常量.
	 */
	private final String m_STYLE = "style";

	/**
	 * <code>values</code> - 节点附加属性值.
	 */
	private Map<String, String> m_values = null;

	/**
	 * 构造方法
	 * 
	 * @param id
	 *            节点ID.
	 */
	public TreeNode(final String id) {
		this(id, false);
	}

	/**
	 * 构造方法.
	 * 
	 * @param id
	 *            节点ID.
	 * @param isExpand
	 *            节点是否展开.
	 */
	public TreeNode(final String id, final boolean isExpand) {
		this.m_id = id;
		this.m_isExpand = isExpand;
		this.m_node = DocumentHelper.createElement("li");
	}

	/**
	 * 构造方法.
	 * 
	 * @param id
	 *            节点ID.
	 * @param values
	 *            节点属性集合.
	 */
	public TreeNode(final String id, final Map<String, String> values) {
		this(id, false, values);
	}

	/**
	 * 构造方法.
	 * 
	 * @param id
	 *            节点id.
	 * @param isExpand
	 *            节点是否展开.
	 * @param values
	 *            节点属性集合.
	 */
	public TreeNode(final String id, final boolean isExpand, final Map<String, String> values) {
		this(id, isExpand);
		this.m_values = values;
	}

	/**
	 * 构造方法.
	 * 
	 * @param id
	 *            节点id.
	 * @param isExpand
	 *            节点是否展开.
	 * @param values
	 *            节点内含值.
	 * @param isLeaf
	 *            节点是否为叶子节点
	 */
	public TreeNode(final String id, final boolean isExpand, final Map<String, String> values, final boolean isLeaf) {
		this(id, isExpand, values);
		this.m_isLeaf = Boolean.valueOf(isLeaf);
	}

	/**
	 * 添加DOM元素内联样式
	 * 
	 * @param el
	 *            DOM节点元素
	 * @param styleValue
	 *            样式文本
	 * @return 返回style样式
	 */
	private Element addStyle(final Element el, final String styleValue) {
		String t_styleVal = el.attributeValue(m_STYLE);

		if (t_styleVal == null) {
			t_styleVal = styleValue;
			el.addAttribute(m_STYLE, t_styleVal);
		} else {
			t_styleVal = t_styleVal + ";" + styleValue;
			el.remove(el.attribute(m_STYLE));
			el.addAttribute(m_STYLE, t_styleVal);
		}
		return el;
	}

	/**
	 * 创建节点Element对象
	 * 
	 * @param hasExpend
	 *            是否具有折叠功能
	 * @param isLast
	 *            是否为最后一个节点
	 * @return 返回li Dom元素
	 */
	public Element create(final boolean hasExpend, final boolean isLast) {
		boolean t_isLeaf = isLeaf();

		String t_cls = null;
		if (isLast && this.m_isExpand && !t_isLeaf) {
			t_cls = s_LASTEXPENDROOT;
		} else if (isLast && !this.m_isExpand && !t_isLeaf) {
			t_cls = s_EXPANDABLE;
		} else if (!isLast && !this.m_isExpand && !t_isLeaf) {
			t_cls = s_EXPANDABLE;
		} else if (!isLast && this.m_isExpand && !t_isLeaf) {
			t_cls = s_COLLAPSABLE;
		} else if (isLast && t_isLeaf) {
			t_cls = s_LAST;
		} else {
			t_cls = "";
		}

		this.m_node.addAttribute("nodeid", this.m_id);

		if (this.m_values != null) {
			Set<String> t_valuekeys = this.m_values.keySet();
			for (Iterator<String> t_iterator = t_valuekeys.iterator(); t_iterator.hasNext();) {
				String t_key = t_iterator.next();
				this.m_node.addAttribute(t_key, this.m_values.get(t_key));
			}
		}

		if (hasExpend) {
			this.m_node.addAttribute("class", t_cls);
			this.m_node.add(Btn.create(isLast, this.m_isExpand, t_isLeaf));
		}

		// 添加节点功能配置，如单选，复选框，文本，按钮等。
		int t_i = 0;
		int t_len = this.m_component.size();
		for (; t_i < t_len; t_i++) {
			try {
				this.m_node.add(m_component.get(t_i));
			} catch (Exception t_e) {
				t_e.printStackTrace();
				;
			}
		}

		// 添加子节点 如果是根节点，有子结点
		if (!t_isLeaf) {
			if (m_childs != null) {
				Element t_ul = DocumentHelper.createElement("ul");
				String t_display = s_COLLAPCLS;
				if (this.m_isExpand) {
					t_display = s_EXPENDCLS;
				}
				this.addStyle(t_ul, t_display);
				boolean t_isLastchild = false;
				int t_j = 0;
				int t_jlen = m_childs.size();
				for (; t_j < t_jlen;) {
					TreeNode t_treeNode = m_childs.get(t_j);
					if (++t_j == t_jlen && t_treeNode.isLeaf()) {
						t_isLastchild = true;
					}
					try {
						t_ul.add(t_treeNode.create(hasExpend, t_isLastchild));
					} catch (Exception t_e) {
						t_e.printStackTrace();
						;
					}
				}
				this.m_node.add(t_ul);
			}
		}
		return this.m_node;
	}

	/**
	 * @return id - {return content description}
	 */
	public String getId() {
		return m_id;
	}

	/**
	 * 异步获得该节点下的子结点.
	 * 
	 * @param hasExpend
	 *            是否折叠
	 * @return 节点HTML字符串
	 */
	public String createAsynTreeNode(final boolean hasExpend) {
		Element t_treeUL = DocumentHelper.createElement("ul");
		boolean t_isLastc = false;
		int t_i = 0;
		int t_len = m_childs.size();
		for (; t_i < t_len;) {
			TreeNode t_treeNode = m_childs.get(t_i);
			if (++t_i == t_len) {
				t_isLastc = true;
			}
			t_treeUL.add(t_treeNode.create(hasExpend, t_isLastc));
		}
		Document t_childs = DocumentHelper.createDocument();
		t_childs.add(t_treeUL);
		return t_childs.asXML();
	}

	/**
	 * 添加节点自定义属性.
	 * 
	 * @param attributes
	 *            节点自定义属性
	 */
	public void setAttributes(final Map<String, String> attributes) {
		m_values = attributes;
	}

	/**
	 * 是否为叶子节点.
	 * 
	 * @return boolean
	 */
	public boolean isLeaf() {
		boolean t_flag = true;
		if (this.m_isLeaf != null) {
			t_flag = this.m_isLeaf.booleanValue();
		} else {
			if (CollectionUtils.isEmpty(m_childs)) {
				t_flag = true;
			} else {
				t_flag = false;
			}
		}
		return t_flag;
	}

	/**
	 * @param isLeaf
	 */

	/**
	 * 设置该节点是否是叶子节点.
	 * 
	 * @param isLeaf
	 *            是否为叶子节点
	 * @return 返回自身
	 */
	public TreeNode setLeaf(final boolean isLeaf) {
		this.m_isLeaf = Boolean.valueOf(isLeaf);
		return this;
	}

	/**
	 * 节点是否可以展开.
	 * 
	 * @return boolean
	 */
	public boolean isExpend() {
		return this.m_isExpand;
	}

	/**
	 * 设置该节点是否展开.
	 * 
	 * @param isExpand
	 *            是否展开
	 * @return 返回自身
	 */
	public TreeNode setExpand(final boolean isExpand) {
		this.m_isExpand = isExpand;
		return this;
	}

	/**
	 * 添加树节点组成部分
	 * 
	 * @param componentELement
	 *            树节点组成部分DOMElement对象
	 * @return 返回自身
	 */
	public TreeNode addCompent(final Element componentELement) {
		this.m_component.add(componentELement);
		return this;
	}

	/**
	 * 添加一个子节点
	 * 
	 * @param node
	 *            子节点TreeNode对象
	 * @return 返回自身
	 */
	public TreeNode addChild(final TreeNode node) {
		if (this.m_childs == null) {
			this.m_childs = new ArrayList<TreeNode>();
		}
		this.m_childs.add(node);
		return this;
	}

	/**
	 * 设置子节点集合
	 * 
	 * @param list
	 *            子节点集合
	 * @return 返回自身
	 */
	public TreeNode setChilds(final List<TreeNode> list) {
		if (list != null && list.size() > 0) {
			int t_i = 0;
			int t_len = list.size();
			for (; t_i < t_len; t_i++) {
				this.addChild(list.get(t_i));
			}
		}
		return this;
	}

	/**
	 * 清除节点下的所有孩子节点.
	 * 
	 * @return 返回自身
	 */
	public TreeNode clearChildren() {
		this.m_childs.clear();
		return this;
	}
}
