package com.chinal.emp.report.util;

import java.util.regex.Pattern;

/**
 * HTML字符串转换工具 <br>
 * <p>
 * Create on : 2012-6-12<br>
 * </p>
 * <br>
 * 
 * @author liuyang<br>
 * @version riil.webcommons v1.0
 *          <p>
 *          <br>
 *          <strong>Modify History:</strong><br>
 *          user modify_date modify_content<br>
 *          -------------------------------------------<br>
 *          <br>
 */
public final class HtmlUtil {
	/**
	 * \r 回车(CR)转义符&amp;#13(支持textarea)
	 */
	public static final String S_LF = "&#13;";
	/**
	 * \n 换行(LF)转义符&amp;#10(支持textarea)
	 */
	public static final String S_CR = "&#10;";
	/**
	 * 换行转义符&lt;br&gt;(支持html)
	 */
	public static final String S_BR = "<br>";

	/**
	 * Constructors.
	 */
	private HtmlUtil() {
	}

	/**
	 * 将输入字符串中带\r\n的内容替换为&#13;&#10;，该方法支持textarea显示
	 * 
	 * @param inputString
	 *            待转字符串
	 * @return 转换后的字符串
	 */
	public static String repaceLFToSymble(final String inputString) {
		if (!"".equals(inputString) && inputString != null) {
			return inputString.replaceAll("\r", S_LF).replace("\n", S_CR);
		} else {
			return "";
		}
	}

	/**
	 * 将输入字符串中带\r\n的内容替换为\<br\> 字符 {method description}.
	 * 
	 * @param inputString
	 *            待替换字符串
	 * @return 转换后的字符串
	 */
	public static String repaceLFToBR(final String inputString) {
		if (!"".equals(inputString) && inputString != null) {
			return inputString.replaceAll("\r\n", S_BR).replace("\r", S_BR).replace("\n", S_BR);
		} else {
			return "";
		}
	}

	/**
	 * 将输入可能包含HTML的文本转成普通文本
	 * 
	 * @param inputString
	 *            待转字符串
	 * @return 转换后的字符串
	 */
	public static String html2Text(final String inputString) {
		String t_htmlStr = inputString;
		String t_textStr = "";
		java.util.regex.Pattern t_pscript;
		java.util.regex.Matcher t_mscript;
		java.util.regex.Pattern t_pstyle;
		java.util.regex.Matcher t_mstyle;
		java.util.regex.Pattern t_phtml;
		java.util.regex.Matcher t_mhtml;

		java.util.regex.Pattern t_phtml1;
		java.util.regex.Matcher t_mhtml1;

		try {
			// 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
			String t_regExScript = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
			// 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
			String t_regExStyle = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
			// 定义HTML标签的正则表达式
			String t_regExHtml = "<[^>]+>";
			String t_regExHtml1 = "<[^>]+";
			t_pscript = Pattern.compile(t_regExScript, Pattern.CASE_INSENSITIVE);
			t_mscript = t_pscript.matcher(t_htmlStr);
			// 过滤script标签
			t_htmlStr = t_mscript.replaceAll("");

			t_pstyle = Pattern.compile(t_regExStyle, Pattern.CASE_INSENSITIVE);
			t_mstyle = t_pstyle.matcher(t_htmlStr);
			// 过滤style标签
			t_htmlStr = t_mstyle.replaceAll("");

			t_phtml = Pattern.compile(t_regExHtml, Pattern.CASE_INSENSITIVE);
			t_mhtml = t_phtml.matcher(t_htmlStr);
			// 过滤html标签
			t_htmlStr = t_mhtml.replaceAll("");

			t_phtml1 = Pattern.compile(t_regExHtml1, Pattern.CASE_INSENSITIVE);
			t_mhtml1 = t_phtml1.matcher(t_htmlStr);
			// 过滤html标签
			t_htmlStr = t_mhtml1.replaceAll("");

			t_textStr = t_htmlStr;

		} catch (Exception t_e) {
			t_e.getMessage();
		}

		return t_textStr;
	}

	/**
	 * 用于字符串显示。将html敏感的尖括号、引号、连接号等用转义符替代。 <br>
	 * 建议用法：在接收到客户端传来的字符串时，不进行转换，直接存入数据库； <br>
	 * 在从数据库中取出，传给客户端做html显示时，才转换。
	 * 
	 * @param input
	 *            需要检查的字符串
	 * @return 转化后的字串
	 */
	public static String convertToHTML(final String input) {
		if (null == input || "".equals(input)) {
			return input;
		}

		StringBuffer t_buf = new StringBuffer();
		char t_ch = ' ';
		for (int t_i = 0; t_i < input.length(); t_i++) {
			t_ch = input.charAt(t_i);
			if (t_ch == '<') {
				t_buf.append("&lt;");
			} else if (t_ch == '>') {
				t_buf.append("&gt;");
			} else if (t_ch == '&') {
				t_buf.append("&amp;");
			} else if (t_ch == '"') {
				t_buf.append("&quot;");
			} else if (t_ch == '\n') {
				t_buf.append("<BR/>");
			} else {
				t_buf.append(t_ch);
			}
		}
		return t_buf.toString();
	}

	/**
	 * 将输入字符串中带><的内容替换为&gt;和&lt; 字符 {method description}.
	 * 
	 * @param str
	 *            待替换的字符串
	 * @return 转换后的字符串
	 */
	public static String repaceFlagLandGToHtml(final String str) {
		if (!"".equals(str) && str != null) {
			return str.replace(">", "&gt;").replace("<", "&lt;");
		} else {
			return "";
		}
	}

	/**
	 * 将输入字符串中带\的内容替换为\\ 字符 {method description}.
	 * 
	 * @param str
	 *            待替换的字符串
	 * @return 转换后的字符串
	 */
	public static String repaceFlagToHtml(final String str) {
		if (!"".equals(str) && str != null) {
			return str.replace("\\", "\\\\");
		} else {
			return "";
		}
	}
}
