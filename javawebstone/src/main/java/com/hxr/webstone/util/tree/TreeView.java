package com.hxr.webstone.util.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.hxr.webstone.util.tree.vo.Ico;
import com.hxr.webstone.util.tree.vo.Image;
import com.hxr.webstone.util.tree.vo.Input;
import com.hxr.webstone.util.tree.vo.Text;
import com.hxr.webstone.util.tree.vo.Tool;
import com.hxr.webstone.util.tree.vo.Tree;
import com.hxr.webstone.util.tree.vo.TreeNode;

/**
 * 生成树方法 <br>
 * <p>
 * Create on : 2011-11-7<br>
 * <p>
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
public class TreeView {
	/**
	 * <code>S_DEFAULTPARENTICON</code> - 默认父节点图标样式.
	 */
	public static final String S_DEFAULTPARENTICON = "ico-file";

	/**
	 * <code>S_DEFAULTLEAFICON</code> - 默认叶子节点图标样式.
	 */
	public static final String S_DEFAULTLEAFICON = "file-open-subset";

	/**
	 * <code>S_TOOLICON</code> - 节点操作按钮样式.
	 */
	private static String s_TOOLICON = "ico-tool";

	/**
	 * <code>m_treeData</code> - 树内容原始对象.
	 */
	private List<? extends Object> m_treeData;
	/**
	 * <code>m_contentProvider</code> - 内容提供者.
	 */
	private IContentProvider m_contentProvider;
	/**
	 * <code>m_labelProvider</code> - 节点内容提供者.
	 */
	private ILabelProvider m_labelProvider;

	/**
	 * <code>m_extendProvider</code> - 扩展内容提供者.
	 */
	private IExtendProvider m_extendProvider;

	/**
	 * <code>m_labelProvider</code> - 树配置对象.
	 */
	private TreeConfig m_config;

	/**
	 * 绘画树.
	 * 
	 * @param treeId
	 *            树ID
	 * @return 树结构HTML字符串
	 * @throws Exception
	 *             异常
	 */
	public String draw(final String treeId) throws Exception {

		List<TreeNode> t_rootList = new ArrayList<TreeNode>();
		createTreeNode(t_rootList);
		Tree t_tree = new Tree(treeId, t_rootList);
		return t_tree.createTree(true);
	}

	/**
	 * 绘画树.
	 * 
	 * @return 树结构HTML字符串
	 * @throws Exception
	 *             异常
	 */
	public String drawAsyNode() throws Exception {

		List<TreeNode> t_rootList = new ArrayList<TreeNode>();
		createTreeNode(t_rootList);
		Tree t_tree = new Tree("", t_rootList);
		return t_tree.createAsynNode(true);
	}

	/**
	 * 创建树.
	 * 
	 * @param rootList
	 *            根元素对象集合
	 * @return List<TreeNode>
	 * @throws Exception
	 *             抛出异常
	 */
	private List<TreeNode> createTreeNode(final List<TreeNode> rootList) throws Exception {
		List<TreeNode> t_rootList = rootList;
		TreeNode t_treeNode;
		List<? extends Object> t_rootObjectList = m_contentProvider.getRootElement(this.m_treeData, 0, 0);
		if (CollectionUtils.isEmpty(t_rootObjectList)) {
			t_rootObjectList = m_treeData;
		}
		for (int t_i = 0; t_i < t_rootObjectList.size(); t_i++) {
			Object t_object = t_rootObjectList.get(t_i);
			t_treeNode = createTreeNode(t_object, 0, t_i);
			recursionNodes(this.m_contentProvider.getChildren(t_object, 0, t_i), t_treeNode, 0);
			t_rootList.add(t_treeNode);
		}
		return t_rootList;
	}

	/**
	 * 递归树节点.
	 * 
	 * @param objectList
	 *            树原始对象集合
	 * @param parentTreeNode
	 *            父树节点
	 * @param level
	 *            树节点集合
	 * @throws Exception
	 *             异常
	 */
	private void recursionNodes(final List<? extends Object> objectList, final TreeNode parentTreeNode, final int level)
			throws Exception {
		int t_level = level + 1;
		TreeNode t_treeNode = null;
		if (objectList != null) {
			for (int t_i = 0; t_i < objectList.size(); t_i++) {
				Object t_childObject = objectList.get(t_i);
				t_treeNode = createTreeNode(t_childObject, t_level, t_i);
				parentTreeNode.addChild(t_treeNode);
				recursionNodes(this.m_contentProvider.getChildren(t_childObject, t_level, t_i), t_treeNode, t_level);
			}
		}
	}

	/**
	 * 创建树节点.
	 * 
	 * @param o
	 *            节点原始对象
	 * @param level
	 *            节点级别
	 * @param index
	 *            节点同级索引
	 * @return 创建的树节点
	 */
	private TreeNode createTreeNode(final Object o, final int level, final int index) {

		TreeNode t_treeNode = new TreeNode(this.m_labelProvider.getLabelId(o, level, index),
				this.m_labelProvider.getAttributes(o, level, index));

		if (this.m_contentProvider.hasChild(o, level, index)) {
			t_treeNode.setLeaf(false);
		} else {
			t_treeNode.setLeaf(true);
		}
		createTreeNodeType(o, level, index, t_treeNode);
		createTreeNodeIcon(o, level, index, t_treeNode);

		t_treeNode.addCompent(Text.create(this.m_labelProvider.isClick(o, level, index),
				this.m_labelProvider.getLabelName(o, level, index)));

		if (this.m_labelProvider.isExtOpra(o, level, index)) {
			if (m_extendProvider == null) {
				t_treeNode.addCompent(Tool.create(s_TOOLICON));
			} else {
				t_treeNode.addCompent(Tool.create(m_extendProvider.getExtendToolStyle(o, level, index)));
			}
		}

		return t_treeNode;

	}

	/**
	 * 创建节点类型.普通类型，复选框类型，单选框类型
	 * 
	 * @param o
	 *            树原始数据
	 * @param level
	 *            树节点级别
	 * @param index
	 *            树节点同级别索引
	 * @param treeNode
	 *            树节点实例
	 */
	private void createTreeNodeType(final Object o, final int level, final int index, final TreeNode treeNode) {
		TreeTypeEnum t_labelType = this.m_labelProvider.getLabelType(o, level, index);
		if (t_labelType != TreeTypeEnum.NORMAL) {
			Map<String, String> t_inputAttr = new HashMap<String, String>();
			if (this.m_labelProvider.isCheck(o, level, index)) {
				t_inputAttr.put("checked", "true");
			}
			treeNode.addCompent(Input.create(t_labelType.value(), t_inputAttr));
		}
	}

	/**
	 * 创建树节点图标.
	 * 
	 * @param o
	 *            树原始数据
	 * @param level
	 *            树节点级别
	 * @param index
	 *            树节点同级别索引
	 * @param treeNode
	 *            树节点实例
	 */
	private void createTreeNodeIcon(final Object o, final int level, final int index, final TreeNode treeNode) {
		String t_icon = this.m_labelProvider.getIcon(o, level, index);
		if (t_icon != null) {
			if (t_icon.indexOf("/") != -1) {
				treeNode.addCompent(Image.create(t_icon));
			} else {
				treeNode.addCompent(Ico.create(t_icon));
			}
		} else if (this.m_labelProvider.isDefaultIcon(o, level, index)) {
			if (treeNode.isLeaf()) {
				treeNode.addCompent(Ico.create(S_DEFAULTLEAFICON));
			} else {
				treeNode.addCompent(Ico.create(S_DEFAULTPARENTICON));
			}
		}
	}

	/**
	 * 设置树原始对象.
	 * 
	 * @param treeData
	 *            树原始对象
	 */
	public void setInput(final List<? extends Object> treeData) {
		this.m_treeData = treeData;
	}

	/**
	 * 设置树原始对象.
	 * 
	 * @param treeData
	 *            树原始对象
	 * @param configId
	 *            公共配置树
	 * @throws Exception
	 *             抛出异常
	 */
	public void setInput(final List<Object> treeData, final String configId) throws Exception {
		this.m_config = TreeConfigManager.getInstance().getTreeConfig(configId);
		if (m_config == null) {
			throw new Exception("tree config file is parse error or not exist.");
		}
		setInput(treeData);
	}

	/**
	 * 设置内容提供者.
	 * 
	 * @param iContentProvider
	 *            内容提供者
	 */
	public void setContentProvider(final IContentProvider iContentProvider) {
		m_contentProvider = iContentProvider;
	}

	/**
	 * 设置树节点配置提供者.
	 * 
	 * @param iLabelProvider
	 *            节点配置提供者
	 */
	public void setLabelProvider(final ILabelProvider iLabelProvider) {
		this.m_labelProvider = iLabelProvider;
	}

	/**
	 * @param extendProvider
	 *            - 扩展配置提供者.
	 */
	public void setExtendProvider(final IExtendProvider extendProvider) {
		m_extendProvider = extendProvider;
	}

}
