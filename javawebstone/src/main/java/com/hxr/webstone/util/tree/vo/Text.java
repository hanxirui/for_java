package com.hxr.webstone.util.tree.vo;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 树节点显示文本 <br>
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
public final class Text {
    /**
     * Constructors.
     */
    private Text() {

    }

    /**
     * 创建显示文本节点.
     * 
     * @param isClick 节点是否可以点击.
     * @param text 显示文字内容.
     * @return span DOM节点.
     */
    public static Element create(final boolean isClick, final String text) {
        return create(isClick, text, false);

    }

    /**
     * 创建显示文本节点.
     * 
     * @param isClick 节点是否可以点击.
     * @param text 显示文字内容.
     * @param isCurrent 是否是当前选中节点.
     * @return span DOM节点.
     */
    public static Element create(final boolean isClick, final String text, final boolean isCurrent) {
        Element t_span = DocumentHelper.createElement("span");
        t_span.addAttribute("type", "text");
        t_span.addText(text);
        if (isCurrent) {
            t_span.addAttribute("isCurrent", "true");
            t_span.addAttribute("class", "treenodecolor-current");
        }
        if (isClick) {
            t_span.addAttribute("style", "cursor:pointer;");
            t_span.addAttribute("clickable", "true");
        } else {
            t_span.addAttribute("style", "cursor:default");
        }
        return t_span;
    }

    /**
     * 创建显示文本节点.
     * 
     * @param isClick 节点是否可以点击.
     * @param txt 显示文字内容.
     * @param isCurrent 是否是当前选中节点.
     * @param title 节点悬浮提示信息.
     * @return span DOM节点.
     */
    public static Element create(final boolean isClick, final String txt, final boolean isCurrent, final String title) {
        Element t_span = create(isClick, txt, isCurrent);
        if (title != null && (!"".equals(title))) {
            t_span.addAttribute("title", title);
        }
        return t_span;
    }

}
