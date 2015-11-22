package com.hxr.webstone.util.tree;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 树配置文件管理类 <br>
 * <p>
 * Create on : 2011-11-7<br>
 * <p>
 * </p>
 * <br>
 * 
 * @author liuyang<br>
 * @version riil.web.common v1.0
 *          <p>
 *          <br>
 *          <strong>Modify History:</strong><br>
 *          user modify_date modify_content<br>
 *          -------------------------------------------<br>
 *          <br>
 */
public final class TreeConfigManager {

	/**
	 * <code>s_instance</code> - 本身实例.
	 */
	private static TreeConfigManager s_instance;
	/**
	 * <code>m_treeConfigPath</code> - tree配置文件路径.
	 */
	private String m_treeConfigPath;
	/**
	 * <code>m_treeConfigMap</code> - 配置文件集合.
	 */
	private Map<String, TreeConfig> m_treeConfigMap = new HashMap<String, TreeConfig>();

	/**
	 * Constructors.
	 */
	private TreeConfigManager() {

	}

	/**
	 * @return treeConfigPath - {return content description}
	 */
	public String getTreeConfigPath() {
		return m_treeConfigPath;
	}

	/**
	 * @param treeConfigPath
	 *            - {parameter description}.
	 */
	public void setTreeConfigPath(final String treeConfigPath) {
		m_treeConfigPath = treeConfigPath;
	}

	/**
	 * 获取单例.
	 * 
	 * @return TreeConfigManager
	 */
	public static TreeConfigManager getInstance() {
		if (s_instance == null) {
			s_instance = new TreeConfigManager();
		}
		return s_instance;
	}

	/**
	 * 加载tree配置文件.
	 * 
	 * @param webappPath
	 *            String
	 */
	public void loadAllTreeConfig(final String webappPath) {
		File t_folder = new File(webappPath, "treeConfig");
		if (!t_folder.exists()) {
			return;
		}
		m_treeConfigPath = t_folder.getAbsolutePath();
		@SuppressWarnings("unchecked")
		Iterator<File> t_files = FileUtils.iterateFiles(t_folder, new String[] { "xml" }, true);
		while (t_files.hasNext()) {
			loadTreeConfig(t_files.next());
		}
	}

	/**
	 * 加载配置文件.
	 * 
	 * @param xmlFile
	 *            File
	 */
	private void loadTreeConfig(final File xmlFile) {
		if (xmlFile == null || !xmlFile.exists()) {

			return;
		}
		TreeConfig t_config = xmlParser(xmlFile);
		if (t_config != null) {
			m_treeConfigMap.put(t_config.getId(), t_config);
		}
	}

	/**
	 * 解析XML.
	 * 
	 * @param xmlFile
	 *            File
	 * @return TreeConfig
	 */
	private TreeConfig xmlParser(final File xmlFile) {
		TreeConfig t_treeConfig = null;
		String t_treeElAttrId = "id";
		try {
			Document t_doc = new SAXReader().read(xmlFile);
			Element t_root = t_doc.getRootElement();
			if (null == t_root.attributeValue(t_treeElAttrId) || "".equals(t_root.attributeValue(t_treeElAttrId))) {
				throw new DocumentException("tree attribute id can not null.");
			}
			t_treeConfig = new TreeConfig();
			t_treeConfig.setId(t_root.attributeValue(t_treeElAttrId));
			String t_contentProvider = t_root.attributeValue("contentProvider");
			String t_labelProvider = t_root.attributeValue("labelProvider");
			t_treeConfig.setContentProvider(loadContentPovider(t_contentProvider));
			t_treeConfig.setLabelProvider(loadLabelProvider(t_labelProvider));
			parseTreeConfig(t_treeConfig, t_root);

			m_treeConfigMap.put(t_treeConfig.getId(), t_treeConfig);
		} catch (DocumentException t_e) {
			t_e.printStackTrace();
		} catch (ClassNotFoundException t_e) {
			t_e.printStackTrace();
		} catch (InstantiationException t_e) {
			t_e.printStackTrace();
		} catch (Exception t_e) {
			t_e.printStackTrace();
		}

		return t_treeConfig;
	}

	/**
	 * 解析XML.
	 * 
	 * @param config
	 *            树配置信息
	 * @param root
	 *            Element 根元素
	 */
	private void parseTreeConfig(final TreeConfig config, final Element root) {
		@SuppressWarnings("unchecked")
		List<Element> t_els = root.selectNodes("/tree/config");
		if (t_els != null && t_els.size() > 0) {
			for (Element t_el : t_els) {
				String t_sync = t_el.attributeValue("isSync");
				if (t_sync != null && t_sync.equals(Boolean.TRUE.toString())) {
					config.setSync(true);
				} else {
					config.setSync(false);
				}
				String t_type = t_el.attributeValue("type");
				if (t_type != null && t_type.equals(TreeConfig.S_TREE_TYPE_NORMAL)) {
					config.setType(TreeConfig.S_TREE_TYPE_NORMAL);
				} else if (t_type != null && t_type.equals(TreeConfig.S_TREE_TYPE_CHECKBOX)) {
					config.setType(TreeConfig.S_TREE_TYPE_CHECKBOX);
				} else if (t_type != null && t_type.equals(TreeConfig.S_TREE_TYPE_RADIO)) {
					config.setType(TreeConfig.S_TREE_TYPE_RADIO);
				} else {
					config.setType(TreeConfig.S_TREE_TYPE_NORMAL);
				}
				String t_isTool = t_el.attributeValue("isTool");
				if (t_isTool != null && t_isTool.equals(Boolean.TRUE.toString())) {
					config.setTool(true);
				} else {
					config.setTool(false);
				}
				String t_isShowIcon = t_el.attributeValue("isShowIcon");
				if (t_isShowIcon != null && t_isShowIcon.equals(Boolean.TRUE.toString())) {
					config.setShowIcon(true);
				} else {
					config.setShowIcon(false);
				}
				String t_isDefaultIcon = t_el.attributeValue("isDefaultIcon");
				if (t_isDefaultIcon != null && t_isDefaultIcon.equals("true")) {
					config.setDefaultIcon(true);
				} else {
					config.setDefaultIcon(false);
				}

			}
		}
	}

	/**
	 * 获取tree配置.
	 * 
	 * @param configId
	 *            配置ID
	 * @return TreeConfig
	 */
	public TreeConfig getTreeConfig(final String configId) {
		if (m_treeConfigMap != null) {
			return m_treeConfigMap.get(configId);
		}
		return null;
	}

	/**
	 * 加载类.
	 * 
	 * @param className
	 *            类名
	 * @return IContentProvider接口实现类
	 * @throws DocumentException
	 *             抛出异常
	 * @throws ClassNotFoundException
	 *             抛出异常
	 * @throws InstantiationException
	 *             抛出异常
	 * @throws IllegalAccessException
	 *             抛出异常
	 */
	private IContentProvider loadContentPovider(final String className)
			throws DocumentException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		if (className == null || className.equals("")) {
			throw new DocumentException("tree attribute contentProvider can not null.");
		}
		Class<?> t_class = Class.forName(className);
		Object t_obj = t_class.newInstance();
		if (t_obj != null && t_obj instanceof IContentProvider) {
			return (IContentProvider) t_obj;
		}
		return null;
	}

	/**
	 * 加载类.
	 * 
	 * @param className
	 *            类名
	 * @return ILabelProvider接口实现类
	 * @throws DocumentException
	 *             抛出异常
	 * @throws ClassNotFoundException
	 *             抛出异常
	 * @throws InstantiationException
	 *             抛出异常
	 * @throws IllegalAccessException
	 *             抛出异常
	 */
	private ILabelProvider loadLabelProvider(final String className)
			throws DocumentException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		if (className == null || className.equals("")) {
			throw new DocumentException("tree attribute labelProvider can not null.");
		}
		Class<?> t_class = Class.forName(className);
		Object t_obj = t_class.newInstance();
		if (t_obj != null && t_obj instanceof ILabelProvider) {
			return (ILabelProvider) t_obj;
		}
		return null;
	}

}
