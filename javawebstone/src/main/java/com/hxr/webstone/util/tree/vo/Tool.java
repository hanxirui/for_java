package com.hxr.webstone.util.tree.vo;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 创建节点操作按钮元素 <br>
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
public final class Tool {
    /**
     * Constructors.
     */
    private Tool() {

    }

    /**
     * 创建节点操作按钮元素.
     * 
     * @param cls 样式
     * @return span Dom元素
     */
    public static Element create(final String cls) {

        Element t_span = DocumentHelper.createElement("span");
        t_span.addAttribute("class", cls);
        t_span.addAttribute("type", "tool");
        t_span.addText(" ");
        return t_span;

    }
}
