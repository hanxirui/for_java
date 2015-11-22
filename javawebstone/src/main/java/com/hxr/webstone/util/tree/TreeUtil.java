package com.hxr.webstone.util.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.hxr.webstone.util.tree.vo.Ico;
import com.hxr.webstone.util.tree.vo.Image;
import com.hxr.webstone.util.tree.vo.Input;
import com.hxr.webstone.util.tree.vo.Text;
import com.hxr.webstone.util.tree.vo.Tool;
import com.hxr.webstone.util.tree.vo.TreeNode;

/**
 * 树常量 <br>
 * <p>
 * Create on : 2011-9-7<br>
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
public abstract class TreeUtil {

	/**
	 * <code>s_CHECKBOX</code> - {复选框树}.
	 */
	public static final String S_CHECKBOX = "checkbox";
	/**
	 * <code>s_RADIO</code> - {单选框树}.
	 */
	public static final String S_RADIO = "radio";
	/**
	 * <code>s_NORMAL</code> - {普通树}.
	 */
	public static final String S_NORMAL = "normal";

	/**
	 * <code>s_DEFAULTPARENTICON</code> - 默认父节点图标样式.
	 */
	public static final String S_DEFAULTPARENTICON = "ico-file";

	/**
	 * <code>s_DEFAULTLEAFICON</code> - 默认叶子节点图标样式.
	 */
	public static final String S_DEFAULTLEAFICON = "file-open-subset";

	/**
	 * <code>s_TOOLICON</code> - 节点操作按钮样式.
	 */
	public static final String S_TOOLICON = "ico-tool";

	/**
	 * 创建树节点.
	 *
	 * @param treeNodeId
	 *            节点ID
	 * @param treeNodeText
	 *            节点文本
	 * @param treeNodeIcon
	 *            节点图标样式
	 * @param treeNodeAttr
	 *            节点自定义属性
	 * @param treeConfig
	 *            树配置信息
	 * @return 节点对象
	 */
	public static TreeNode createTreeNode(final String treeNodeId, final String treeNodeText, final String treeNodeIcon,
			final Map<String, String> treeNodeAttr, final TreeConfig treeConfig) {

		TreeNode t_treeNode = new TreeNode(treeNodeId, false, treeNodeAttr);

		if (!treeConfig.isLeaf()) {
			t_treeNode.setLeaf(false);
		}
		t_treeNode.setExpand(treeConfig.isExpand());

		if (!TreeUtil.S_NORMAL.equals(treeConfig.getTreeType())) {
			Map<String, String> t_inputAttr = new HashMap<String, String>();
			String[] t_selecteds = treeConfig.getSelecteds();
			if (t_selecteds != null) {
				int t_i = 0;
				int t_len = t_selecteds.length;
				for (; t_i < t_len; t_i++) {
					if (t_selecteds[t_i].equals(treeNodeId)) {
						t_inputAttr.put("checked", "true");
						break;
					}
				}
			}
			if (treeConfig.isDisabled()) {
				t_inputAttr.put("disabled", "disabled");
			}
			t_treeNode.addCompent(Input.create(treeConfig.getTreeType(), t_inputAttr));
		}

		if (treeConfig.isIcon()) {
			if (treeConfig.isImage()) {
				t_treeNode.addCompent(Image.create(treeNodeIcon));
			} else {
				t_treeNode.addCompent(Ico.create(treeNodeIcon));
			}

		}

		t_treeNode.addCompent(Text.create(treeConfig.isClick(), treeNodeText, false,
				treeConfig.getTitle() == null ? treeNodeText : treeConfig.getTitle()));

		if (treeConfig.isTool()) {
			t_treeNode.addCompent(Tool.create(TreeUtil.S_TOOLICON));
		}

		return t_treeNode;

	}

	/**
	 * 创建自定义根节点.
	 *
	 * @param treeConfig
	 *            树配置对象
	 * @param childTreeNodeList
	 *            当前所有子节点
	 * @return 根节点集合
	 */
	public static List<TreeNode> createRootNode(final TreeConfig treeConfig, final List<TreeNode> childTreeNodeList) {
		if (treeConfig.getRootText() != null) {
			String t_treeNodeIcon = StringUtils.isBlank(treeConfig.getRootIconClass()) ? TreeUtil.S_DEFAULTPARENTICON
					: treeConfig.getRootIconClass();
			TreeNode t_rootNode = createTreeNode(treeConfig.getRootId(), treeConfig.getRootText(), t_treeNodeIcon, null,
					treeConfig);
			t_rootNode.setChilds(childTreeNodeList);
			List<TreeNode> t_rootNodeList = new ArrayList<TreeNode>();
			t_rootNodeList.add(t_rootNode);
			return t_rootNodeList;
		} else {
			return childTreeNodeList;
		}
	}

	/**
	 * 获取树类型.
	 *
	 * @param treeType
	 *            树类型
	 * @return 树类型
	 */
	public static String getTreeType(final String treeType) {
		if (S_CHECKBOX.equalsIgnoreCase(treeType)) {
			return S_CHECKBOX;
		} else if (S_RADIO.equalsIgnoreCase(treeType)) {
			return S_RADIO;
		} else {
			return S_NORMAL;
		}
	}
}
