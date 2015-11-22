package com.hxr.webstone.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.hxr.webstone.pojo.TreeDemoPojo;
import com.hxr.webstone.util.tree.TreeConfig;
import com.hxr.webstone.util.tree.TreeUtil;
import com.hxr.webstone.util.tree.vo.Tree;
import com.hxr.webstone.util.tree.vo.TreeNode;

/**
 * 创建地理位置树 <br>
 * <p>
 * Create on : 2011-9-6<br>
 * </p>
 * <br>
 *
 * @author fangzhixin<br>
 * @version riil_web_common v1.0 <br>
 *          <strong>Modify History:</strong><br>
 *          user modify_date modify_content<br>
 *          -------------------------------------------<br>
 *          <br>
 */
public class DemoTree extends TreeUtil {

	/**
	 * 地理位置创建方法.
	 *
	 * @param treeId
	 *            树ID
	 * @param demoTrees
	 *            地理位置Pojo集合
	 * @param treeConfig
	 *            树配置对象
	 * @return 树HTML字符串
	 */
	public static String create(final String treeId, final List<TreeDemoPojo> demoTrees, final TreeConfig treeConfig) {

		TreeConfig t_treeConfig = treeConfig;
		if (t_treeConfig == null) {
			t_treeConfig = new TreeConfig();
		}

		TreeDemoPojo t_treePojo = null;
		TreeNode t_treeNode = null;
		TreeNode t_parentTreeNode = null;
		List<TreeNode> t_rootNodeList = new ArrayList<TreeNode>();
		Map<String, String> t_treeNodeAttr = null;

		Map<String, TreeNode> t_treeNodes = new HashMap<String, TreeNode>();
		Map<String, TreeDemoPojo> t_locationPojoMap = getMap(demoTrees);
		Map<String, TreeDemoPojo> t_locationHasChildrenMap = getHasChildrenMap(demoTrees);

		String t_id = null;
		String t_parentId = null;
		String t_icon = null;
		String t_defaultIcon = null;

		int t_i = 0;
		int t_len = demoTrees.size();
		for (; t_i < t_len; t_i++) {
			t_treePojo = demoTrees.get(t_i);
			t_treeNodeAttr = new HashMap<String, String>();
			t_treeNodeAttr.put("customizedAttr", "customizedAttr" + t_i);
			t_treeNodeAttr.put("onselectstart", "return false;");

			t_id = t_treePojo.getId();
			t_parentId = t_treePojo.getParentId();

			boolean t_isParent = false;

			if (t_parentId == null || "".equals(t_parentId)) {
				t_isParent = true;
				t_defaultIcon = DemoTree.S_DEFAULTPARENTICON;
			} else {
				t_parentTreeNode = t_treeNodes.get(t_parentId);
				if (t_parentTreeNode == null) {
					t_isParent = true;
				}
			}
			if (t_locationHasChildrenMap.get(t_id) != null) {
				t_defaultIcon = DemoTree.S_DEFAULTPARENTICON;
			} else {
				t_defaultIcon = DemoTree.S_DEFAULTLEAFICON;
			}
			if (t_icon == null) {
				t_icon = t_defaultIcon;
				t_treeConfig.setDefaultIcon(true);
			}

			t_treeNode = DemoTree.createTreeNode(t_id,
					"".equals(t_treePojo.getName()) || t_treePojo.getName() == null ? "-"
							: t_treePojo.getName(),
					t_icon, t_treeNodeAttr, t_treeConfig);
			t_icon = null;
			if (t_isParent) {
				t_rootNodeList.add(t_treeNode);
			} else {
				t_parentTreeNode.addChild(t_treeNode);
			}
			for (Entry<String, TreeNode> t_entry : t_treeNodes.entrySet()) {
				String t_key = t_entry.getKey();
				TreeNode t_value = t_entry.getValue();
				if (t_id.equals(t_locationPojoMap.get(t_key).getParentId())) {
					t_treeNode.addChild(t_value);
				}
			}
			t_treeNodes.put(t_id, t_treeNode);
		}
		t_rootNodeList = DemoTree.createRootNode(t_treeConfig, t_rootNodeList);

		Tree t_tree = new Tree(treeId, t_rootNodeList);
		return t_tree.createTree(true);

	}

	/**
	 * 将地理位置list转换为map格式，并以地理位置实体ID做为map的键值
	 *
	 * @param locationList
	 *            待转换的地理位置列表
	 * @return 地理位置map
	 */
	private static Map<String, TreeDemoPojo> getMap(final List<TreeDemoPojo> locationList) {
		Map<String, TreeDemoPojo> t_PojoMap = new HashMap<String, TreeDemoPojo>();
		for (TreeDemoPojo t_item : locationList) {
			t_PojoMap.put(t_item.getId(), t_item);
		}
		return t_PojoMap;
	}

	/**
	 * 对所提供的地理位置数据取含有子地理位置的实体
	 *
	 * @param locationList
	 *            地理位置列表
	 * @return 含有子地理位置的实体map
	 */
	private static Map<String, TreeDemoPojo> getHasChildrenMap(final List<TreeDemoPojo> locationList) {
		Map<String, TreeDemoPojo> t_PojoMap = getMap(locationList);
		Map<String, TreeDemoPojo> t_HasChildren = new HashMap<String, TreeDemoPojo>();
		for (TreeDemoPojo t_item : locationList) {
			if (t_PojoMap.containsKey(t_item.getParentId())) {
				t_HasChildren.put(t_item.getParentId(), t_PojoMap.get(t_item.getParentId()));
			}
		}
		return t_HasChildren;
	}

}
