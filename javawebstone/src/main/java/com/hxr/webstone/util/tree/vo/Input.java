package com.hxr.webstone.util.tree.vo;

import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 树节点复选框或单选框 <br>
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
public final class Input {
    /**
     * Constructors.
     */
    private Input() {

    }

    /**
     * 创建复选框或单选框元素.
     * 
     * @param type 复选框或单选框类型 checkbox/radio
     * @param attr 附加属性
     * @return Element 返回checkbo或radio元素节点
     */
    public static Element create(final String type, final Map<String, String> attr) {
        Element t_input = DocumentHelper.createElement("input");
        t_input.addAttribute("type", type);
        t_input.addAttribute("name", "treeNodeType");
        if (attr != null) {
            for (Entry<String, String> t_entry : attr.entrySet()) {
                String t_attrKey = t_entry.getKey();
                String t_attrVal = t_entry.getValue();
                t_input.addAttribute(t_attrKey, t_attrVal);
            }

            // Set<String> t_attrKeys = attr.keySet();
            // for (Iterator t_iterator = t_attrKeys.iterator(); t_iterator.hasNext();) {
            // String t_attrKey = (String) t_iterator.next();
            // String t_attrVal = attr.get(t_attrKey);
            // t_input.addAttribute(t_attrKey, t_attrVal);
            // }
        }
        return t_input;
    }

    /**
     * 创建复选框或单选框元素.
     * 
     * @param type 复选框或单选框类型 checkbox/radio
     * @return Element 返回checkbo或radio元素节点
     */
    public static Element create(final String type) {
        return create(type, null);
    }

}
