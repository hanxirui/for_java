package com.hxr.webstone.util.tree;

/**
 * 创建树配置类 <br>
 * <p>
 * Create on : 2011-9-9<br>
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
public class TreeConfig {

	/**
	 * <code>S_DEFAULT_TREE_CONFIG</code> - 用于默认常量.
	 */
	public static final TreeConfig S_DEFAULT_TREE_CONFIG = new TreeConfig();

	/**
	 * <code>isIcon</code> - 树节点是否显示图标.
	 */
	private boolean m_isIcon;
	/**
	 * <code>rootIconClass</code> - 树根节点显示图标样式.
	 */
	private String m_rootIconClass;

	/**
	 * <code>isIcon</code> - 树节点是否显示图标使用Image.
	 */
	private boolean m_isImage;

	/**
	 * <code>m_isDefaultIcon</code> - 是否使用默认图标.
	 */
	private boolean m_isDefaultIcon;

	/**
	 * <code>isTool</code> - 树节点是否有操作按钮.
	 */
	private boolean m_isTool;

	/**
	 * <code>isExpand</code> - 节点是否展开，该属性用于生成某一个树节点.
	 */
	private boolean m_isExpand;

	/**
	 * <code>isLeaf</code> - 是否是叶子节点，默认true，该属性用于生成某一个树节点.
	 */
	private boolean m_isLeaf = true;

	/**
	 * <code>m_isClick</code> - 节点是否可以点击，默认true该属性用于生成某一个树节点.
	 */
	private boolean m_isClick = true;

	/**
	 * <code>m_isDisabled</code> -节点是否不可用.
	 */
	private boolean m_isDisabled = false;

	/**
	 * <code>title</code> - 节点悬浮提示.
	 */
	private String m_title = null;
	/**
	 * <code>treeType</code> - 树类型.
	 */
	private String m_treeType = TreeUtil.S_NORMAL;

	/**
	 * <code>rootText</code> - 根节点文本.
	 */
	private String m_rootText;

	/**
	 * <code>rootId</code> - 根节点ID.
	 */
	private String m_rootId;

	/**
	 * <code>m_selecteds</code> - 选中节点ID数组集合.
	 */
	private String[] m_selecteds = null;

	/**
	 * Constructors.
	 */
	public TreeConfig() {

	}

	/**
	 * <code>S_TREE_TYPE_NORMAL</code> - 普通的树.
	 */
	public static final String S_TREE_TYPE_NORMAL = "normal";
	/**
	 * <code>S_TREE_TYPE_CHECKBOX</code> - 可多选的树.
	 */
	public static final String S_TREE_TYPE_CHECKBOX = "checkbox";
	/**
	 * <code>S_TREE_TYPE_RADIO</code> - 单选的树.
	 */
	public static final String S_TREE_TYPE_RADIO = "radio";

	/**
	 * <code>m_id</code> - 树ID.
	 */
	private String m_id;
	/**
	 * <code>m_contentProvider</code> - 树内容数据提供者接口.
	 */
	private IContentProvider m_contentProvider;
	/**
	 * <code>m_labelProvider</code> - 树节点显示提供者接口.
	 */
	private ILabelProvider m_labelProvider;
	/**
	 * <code>isSync</code> - 是否为同步.
	 */
	private boolean m_isSync;
	/**
	 * <code>type</code> - 树类型.
	 */
	private String m_type;

	/**
	 * <code>isShowIcon</code> - 是否显示图标.
	 */
	private boolean m_isShowIcon;

	/**
	 * @return id - {return content description}
	 */
	public String getId() {
		return m_id;
	}

	/**
	 * @param id
	 *            - {parameter description}.
	 */
	public void setId(final String id) {
		m_id = id;
	}

	/**
	 * @return contentProvider - {return content description}
	 */
	public IContentProvider getContentProvider() {
		return m_contentProvider;
	}

	/**
	 * @param contentProvider
	 *            - {parameter description}.
	 */
	public void setContentProvider(final IContentProvider contentProvider) {
		m_contentProvider = contentProvider;
	}

	/**
	 * @return labelProvider - {return content description}
	 */
	public ILabelProvider getLabelProvider() {
		return m_labelProvider;
	}

	/**
	 * @param labelProvider
	 *            - {parameter description}.
	 */
	public void setLabelProvider(final ILabelProvider labelProvider) {
		m_labelProvider = labelProvider;
	}

	/**
	 * @return isSync - {return content description}
	 */
	public boolean isSync() {
		return m_isSync;
	}

	/**
	 * @param isSync
	 *            - {parameter description}.
	 */
	public void setSync(final boolean isSync) {
		this.m_isSync = isSync;
	}

	/**
	 * @return type - {return content description}
	 */
	public String getType() {
		return m_type;
	}

	/**
	 * @param type
	 *            - {parameter description}.
	 */
	public void setType(final String type) {
		this.m_type = type;
	}

	/**
	 * @return isShowIcon - {return content description}
	 */
	public boolean isShowIcon() {
		return m_isShowIcon;
	}

	/**
	 * @param isShowIcon
	 *            - {parameter description}.
	 */
	public void setShowIcon(final boolean isShowIcon) {
		this.m_isShowIcon = isShowIcon;
	}

	/**
	 * Constructors.
	 *
	 * @param treeType
	 *            树类型
	 */
	private TreeConfig(final String treeType) {
		m_treeType = TreeUtil.getTreeType(treeType);
	}

	/**
	 * 获取悬浮提示信息.
	 *
	 * @return 悬浮提示信息
	 */
	public String getTitle() {
		return m_title;
	}

	/**
	 * 设置悬浮提示信息.
	 *
	 * @param title
	 *            悬浮提示信息
	 */
	public void setTitle(final String title) {
		this.m_title = title;
	}

	/**
	 * 默认可用(false)
	 *
	 * @return true/false
	 */
	public boolean isDisabled() {
		return m_isDisabled;
	}

	/**
	 * 设置是否不可用(默认false)
	 *
	 * @param isDisabled
	 *            是否不可用
	 */
	public void setDisabled(final boolean isDisabled) {
		this.m_isDisabled = isDisabled;
	}

	/**
	 * 根据树类型创建树配置对象.
	 *
	 * @param treeType
	 *            树类型
	 * @return TreeConfig
	 */
	public static TreeConfig createTreeConfigByTreeType(final String treeType) {
		return new TreeConfig(treeType);
	}

	/**
	 * {method description}.
	 *
	 * @return String
	 */
	public boolean isIcon() {
		return m_isIcon;
	}

	/**
	 * {method description}.
	 *
	 * @param isIcon
	 *            树节点是否显示图标
	 */
	public void setIcon(final boolean isIcon) {
		m_isIcon = isIcon;
	}

	/**
	 * {method description}.
	 *
	 * @return String
	 */
	public boolean isTool() {
		return m_isTool;
	}

	/**
	 * {method description}.
	 *
	 * @param isTool
	 *            是否有操作按钮
	 */
	public void setTool(final boolean isTool) {
		m_isTool = isTool;
	}

	/**
	 * {method description}.
	 *
	 * @return String
	 */
	public String getTreeType() {
		return m_treeType;
	}

	/**
	 * {method description}.
	 *
	 * @param treeType
	 *            树类型
	 */
	public void setTreeType(final String treeType) {
		m_treeType = treeType;
	}

	/**
	 * {method description}.
	 *
	 * @return String
	 */
	public String getRootText() {
		return m_rootText;
	}

	/**
	 * {method description}.
	 *
	 * @param rootText
	 *            根节点文本
	 */
	public void setRootText(final String rootText) {
		m_rootText = rootText;
	}

	/**
	 * {method description}.
	 *
	 * @return String
	 */
	public String getRootId() {
		return m_rootId;
	}

	/**
	 * {method description}.
	 *
	 * @param rootId
	 *            根节点ID
	 */
	public void setRootId(final String rootId) {
		m_rootId = rootId;
	}

	/**
	 * 是否使用默认图标.
	 *
	 * @return String
	 */
	public boolean isDefaultIcon() {
		return m_isDefaultIcon;
	}

	/**
	 * 是否使用默认图标.
	 *
	 * @param isDefaultIcon
	 *            是否使用默认图标
	 */
	public void setDefaultIcon(final boolean isDefaultIcon) {
		m_isDefaultIcon = isDefaultIcon;
	}

	/**
	 * 选中节点ID数组集合.
	 *
	 * @return String[]
	 */
	public String[] getSelecteds() {
		return m_selecteds;
	}

	/**
	 * 获得选中节点ID集合.
	 *
	 * @param selecteds
	 *            选中节点ID数组
	 */
	public void setSelecteds(final String[] selecteds) {
		this.m_selecteds = selecteds;
	}

	/**
	 * {method description}.
	 *
	 * @return boolean
	 */
	public boolean isExpand() {
		return m_isExpand;
	}

	/**
	 * {method description}.
	 *
	 * @param isExpand
	 *            boolean
	 */
	public void setExpand(final boolean isExpand) {
		m_isExpand = isExpand;
	}

	/**
	 * {method description}.
	 *
	 * @return boolean
	 */
	public boolean isLeaf() {
		return m_isLeaf;
	}

	/**
	 * {method description}.
	 *
	 * @param isLeaf
	 *            boolean
	 */
	public void setLeaf(final boolean isLeaf) {
		m_isLeaf = isLeaf;
	}

	/**
	 * {method description}.
	 *
	 * @return boolean
	 */
	public boolean isClick() {
		return m_isClick;
	}

	/**
	 * {method description}.
	 *
	 * @param isClick
	 *            boolean
	 */
	public void setClick(final boolean isClick) {
		this.m_isClick = isClick;
	}

	/**
	 * 获取树节点是否显示图标使用Image.
	 *
	 * @return true/false
	 */
	public boolean isImage() {
		return m_isImage;
	}

	/**
	 * 设置树节点是否显示图标使用Image.
	 *
	 * @param isImage
	 *            树节点是否显示图标使用Image.
	 */
	public void setImage(final boolean isImage) {
		this.m_isImage = isImage;
	}

	/**
	 * 获取树根节点显示图标样式.
	 *
	 * @return 样式名
	 */
	public String getRootIconClass() {
		return m_rootIconClass;
	}

	/**
	 * 设置树根节点显示图标样式.
	 *
	 * @param rootIconClass
	 *            样式名
	 */
	public void setRootIconClass(final String rootIconClass) {
		m_rootIconClass = rootIconClass;
	}

}
