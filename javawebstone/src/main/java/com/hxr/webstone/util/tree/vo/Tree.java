package com.hxr.webstone.util.tree.vo;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 树 <br>
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
 * <br>
 */
public class Tree {
    /**
     * <code>S_CLASS_STR</code> - class字符串常量.
     */
    protected static final String S_CLASS_STR = "class";
    /**
     * <code>childs</code> - 树子节点.
     */
    private List<TreeNode> m_childs = new ArrayList<TreeNode>();

    /**
     * 树ID
     */
    private String m_id = "";

    /**
     * Constructors.
     *
     * @param id 树ID
     */
    public Tree(final String id) {
        this.m_id = id;
    }

    /**
     * Constructors.
     *
     * @param id 树ID
     * @param childs 子节点TreeNode
     */
    public Tree(final String id, final List<TreeNode> childs) {
        this.m_childs = childs;
        this.m_id = id;
    }

    /**
     * 设置树的子节点.
     *
     * @param childs 子节点TreeNode
     */
    public void setChilds(final List<TreeNode> childs) {
        this.m_childs = childs;
    }

    /**
     * 添加子节点
     *
     * @param treeNode TreeNode节点对象
     */
    public void addChild(final TreeNode treeNode) {
        this.m_childs.add(treeNode);
    }

    /**
     * 创建树HTML
     *
     * @param hasExpand 是否有折叠功能
     * @return 树的HTML结构字符串
     */
    public String createTree(final boolean hasExpand) {

        Element t_treeUL = DocumentHelper.createElement("ul");
        t_treeUL.addAttribute("id", this.m_id);

        t_treeUL.addAttribute(S_CLASS_STR, "tree-wrap");

        Element t_rootLI = DocumentHelper.createElement("li");
        t_rootLI.addAttribute("nodeid", "root");

        Element t_ul = createRoot(hasExpand);
        if (t_ul != null) {
            t_rootLI.add(t_ul);
        }
        t_treeUL.add(t_rootLI);
        Document t_tree = DocumentHelper.createDocument();
        t_tree.add(t_treeUL);

        return t_tree.asXML();
    }

    /**
     * 创建树 {method description}.
     *
     * @param hasExpand 是否展开
     * @return t_treeUL Element
     */
    protected Element createRoot(final boolean hasExpand) {
        Element t_ul = DocumentHelper.createElement("ul");
        t_ul.addAttribute(S_CLASS_STR, "treeview");
        int t_childsize = this.m_childs.size();
        if (t_childsize > 0) {
            if (!hasExpand) {
                t_ul.addAttribute(S_CLASS_STR, "nolinetreeview");

            }
            boolean t_isLast = false;
            for (int t_i = 0; t_i < t_childsize; t_i++) {
                TreeNode t_treeNode = this.m_childs.get(t_i);
                if (t_i == t_childsize - 1) {
                    t_isLast = true;
                } else {
                    t_isLast = false;
                }
                t_ul.add(t_treeNode.create(hasExpand, t_isLast));
            }

        }
        return t_ul;
    }

    /**
     * 异步加载树节点片段
     *
     * @param hasExpand 是否有折叠功能
     * @return 树的HTML结构字符串
     */
    public String createAsynNode(final boolean hasExpand) {
        Document t_tree = DocumentHelper.createDocument();

        Element t_root = createRoot(true);
        t_tree.add(t_root);
        return t_tree.asXML();
    }

    /**
     * @return id - {return content description}
     */
    public String getId() {
        return m_id;
    }
}
