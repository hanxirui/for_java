package com.hxr.webstone.util.tree.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ez.framework.core.service.ServiceException;
import com.hxr.webstone.pojo.TreeDemoPojo;
import com.hxr.webstone.util.tree.IContentProvider;
import com.hxr.webstone.util.tree.ILabelProvider;
import com.hxr.webstone.util.tree.TreeTypeEnum;

/**
 * 地理位置视图树 <br>
 * <p>
 * Create on : 2011-11-8<br>
 * </p>
 * <br>
 * 
 * @author fangzhixin<br>
 * @version riil.web.common v1.0 <br>
 *          <strong>Modify History:</strong><br>
 *          user modify_date modify_content<br>
 *          -------------------------------------------<br>
 *          <br>
 */
public class DemoTreeProvider implements IContentProvider, ILabelProvider {
	/**
	 * <code>s_ALL</code> - 全部节点id.
	 */
	private static final String S_ALL = "all";
	/**
	 * <code>treeData</code> - tree数据.
	 */
	private List<? extends Object> m_treeData;

	/**
	 * <code>m_firstData</code> - 第一级节点.
	 */
	private List<Object> m_firstData;

	/**
	 * <code>m_labelType</code> - {description}.
	 */
	private TreeTypeEnum m_labelType = TreeTypeEnum.NORMAL;

	/**
	 * <code>locationPojoParentMap</code> - 按照父子关系整理key是父ID,value:为该父节点的子节点集合.
	 */
	private final Map<String, List<Object>> m_locationPojoParentMap = new HashMap<String, List<Object>>();

	/**
	 * Constructors.
	 */
	public DemoTreeProvider() {

	}

	/**
	 * Constructors.
	 * 
	 * @param labelType
	 *            labelType
	 */
	public DemoTreeProvider(final TreeTypeEnum labelType) {
		this.m_labelType = labelType;
	}

	public TreeTypeEnum getLabelType(final Object curElement, final int level, final int index) {
		return m_labelType;
	}

	public String getLabelName(final Object curElement, final int level, final int index) {
		return ((TreeDemoPojo) curElement).getName();
	}

	public String getLabelId(final Object curElement, final int level, final int index) {
		return ((TreeDemoPojo) curElement).getId();
	}

	public String getIcon(final Object curElement, final int level, final int index) {
		return null;
	}

	public boolean isDefaultIcon(final Object curElement, final int level, final int index) {
		return false;
	}

	public boolean isExtOpra(final Object curElement, final int level, final int index) {
		return false;
	}

	public boolean isClick(final Object curElement, final int level, final int index) {
		return true;
	}

	public boolean isCheck(final Object curElement, final int level, final int index) {
		return false;
	}

	public String getCheckId(final Object curElement, final int level, final int index) {
		return null;
	}

	public Map<String, String> getAttributes(final Object curElement, final int level, final int index) {
		return null;
	}

	public List<Object> getChildren(final Object curElement, final int level, final int index) throws ServiceException {
		TreeDemoPojo t_curLocationPojo = (TreeDemoPojo) curElement;
		if (S_ALL.equals(t_curLocationPojo.getId())) {
			return this.m_firstData;
		} else {
			return this.m_locationPojoParentMap.get(t_curLocationPojo.getId());
		}
	}

	public List<Object> getRootElement(final List<? extends Object> treeData, final int level, final int index) {
		this.m_treeData = treeData;
		// TreeDemoPojo t_allLocationPojo = new TreeDemoPojo();
		// t_allLocationPojo.setId(S_ALL);
		// t_allLocationPojo.setName(CustomeMessageSource.getMessage("label.all"));

		// List<Object> t_returnList = new ArrayList<Object>();
		// t_returnList.add(t_allLocationPojo);
		this.m_firstData = clearUpTreeData(this.m_treeData);

		return this.m_firstData;
	}

	public boolean hasChild(final Object curElement, final int level, final int index) {
		// if (m_locationPojoParentMap == null) {
		// return false;
		// }
		// TreeDemoPojo t_locationPojo = (TreeDemoPojo) curElement;
		// if (S_ALL.equals(t_locationPojo.getId())) {
		// return true;
		// } else {
		// return m_locationPojoParentMap.get(((TreeDemoPojo)
		// curElement).getId()).size() > 0 ? true : false;
		// }
		return true;

	}

	/**
	 * 将树原始数据整理为Map形式:key:节点ID,value，子节点集合.
	 * 
	 * @param treeData
	 *            树原始节点
	 * @return 树第一级节点集合
	 */
	private List<Object> clearUpTreeData(final List<? extends Object> treeData) {
		if (this.m_treeData == null) {
			return null;
		}
		Map<String, Object> t_treeDataMap = list2Map(this.m_treeData);
		List<Object> t_firstLevelList = new ArrayList<Object>();
		for (int t_i = 0; t_i < this.m_treeData.size(); t_i++) {
			TreeDemoPojo t_treeDemoPojo = (TreeDemoPojo) this.m_treeData.get(t_i);
			String t_id = t_treeDemoPojo.getId();
			String t_parentId = t_treeDemoPojo.getParentId();

			if (m_locationPojoParentMap.get(t_id) == null) {
				m_locationPojoParentMap.put(t_id, new ArrayList<Object>());
			}
			if (t_treeDataMap.containsKey(t_parentId)) {
				if (m_locationPojoParentMap.get(t_parentId) == null) {
					m_locationPojoParentMap.put(t_parentId, new ArrayList<Object>());
				}
				m_locationPojoParentMap.get(t_parentId).add(t_treeDemoPojo);
			}
			if (t_parentId == null || "".equals(t_parentId) || "-1".equals(t_parentId)
					|| !t_treeDataMap.containsKey(t_parentId)) {
				t_firstLevelList.add(t_treeDemoPojo);
			}
		}
		return t_firstLevelList;
	}

	/**
	 * 将数据List形式转换为Map方式 key为id,value：pojo对象.
	 * 
	 * @param dataList
	 *            数据List形式
	 * @return 数据Map形式
	 */
	private Map<String, Object> list2Map(final List<? extends Object> dataList) {
		Map<String, Object> t_PojoMap = new HashMap<String, Object>();
		for (Object t_item : dataList) {
			t_PojoMap.put(((TreeDemoPojo) t_item).getId(), t_item);
		}
		return t_PojoMap;
	}

}
