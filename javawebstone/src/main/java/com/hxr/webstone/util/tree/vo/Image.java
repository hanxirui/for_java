package com.hxr.webstone.util.tree.vo;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 树节点图标IMAGE类 <br>
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
public final class Image {
    /**
     * Constructors.
     */
    private Image() {

    }

    /**
     * 创建图标元素.
     * 
     * @param icoName 图标样式名.
     * @return span DOM元素.
     */
    public static Element create(final String icoName) {

        Element t_image = DocumentHelper.createElement("img");
        t_image.addAttribute("src", icoName);
        return t_image;
    }
}
