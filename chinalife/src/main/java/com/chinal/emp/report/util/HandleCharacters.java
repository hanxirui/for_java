package com.chinal.emp.report.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 特殊字符处理帮助工具 <br>
 * <p>
 * Create on : 2013-3-18<br>
 * </p>
 * <br>
 * 
 * @author zhengweiqiang@ruijie.com.cn<br>
 * @version riil.common.biz.api v6.2.0 <br>
 *          <strong>Modify History:</strong><br>
 *          user modify_date modify_content<br>
 *          -------------------------------------------<br>
 *          <br>
 */
public final class HandleCharacters {

	/**
	 * <code>S_HTML_SPECIAL_CHARACTER_MAP</code> - html中特殊字符的映射关系 .
	 */
	private static final Map<String, String> S_HTML_SPECIAL_CHARACTER_MAP = new HashMap<String, String>();

	static {
		S_HTML_SPECIAL_CHARACTER_MAP.put("&", "&amp;");
		S_HTML_SPECIAL_CHARACTER_MAP.put("\"", "&quot;");
		S_HTML_SPECIAL_CHARACTER_MAP.put("<", "&lt;");
		S_HTML_SPECIAL_CHARACTER_MAP.put(">", "&gt;");
	}

	/**
	 * 构造.
	 */
	private HandleCharacters() {
	}

	/**
	 * 处理特殊字符资源名称.
	 * 
	 * @param str
	 *            传入值
	 * @return 处理后的值
	 */
	public static String handleSpecialcharacters(final String str) {
		if (StringUtils.isNotEmpty(str)) {
			return str.replace(">", "＞").replace("<", "＜").replace("\"", "＂").replace("'", "＇");
		} else {
			return "";
		}
	}

	/**
	 * 
	 * 转化html特殊字符
	 * 
	 * @param input
	 *            输入
	 * @param excludeChars
	 *            不进行转换的字符
	 * @return String
	 */
	public static String htmlCharacterEscape(final String input, final char[] excludeChars) {
		if (input != null) {
			StringBuilder t_escaped = new StringBuilder(input.length() * 2);
			for (int t_i = 0; t_i < input.length(); t_i++) {
				char t_character = input.charAt(t_i);
				String t_reference = null;
				if (!ArrayUtils.contains(excludeChars, t_character)) {
					t_reference = S_HTML_SPECIAL_CHARACTER_MAP.get(String.valueOf(t_character));
				}
				if (t_reference != null) {
					t_escaped.append(t_reference);
				} else {
					t_escaped.append(t_character);
				}
			}
			return t_escaped.toString();
		} else {
			return null;
		}
	}
}
