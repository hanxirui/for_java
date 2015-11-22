package com.hxr.webstone.util.tree.vo;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 树节点图标工具类 <br>
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
public final class Ico {
    /**
     * Constructors.
     */
    private Ico() {

    }

    /**
     * 创建图标元素.
     * 
     * @param cls 图标样式名.
     * @return span DOM元素.
     */
    public static Element create(final String cls) {

        Element t_span = DocumentHelper.createElement("span");
        t_span.addAttribute("class", cls);
        t_span.addAttribute("type", "ico");
        t_span.addAttribute("style", "cursor:default");
        t_span.addText(" ");
        return t_span;

    }
}
