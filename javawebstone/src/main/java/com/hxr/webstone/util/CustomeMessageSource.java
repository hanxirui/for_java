package com.hxr.webstone.util;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.NoSuchMessageException;

public class CustomeMessageSource implements MessageSourceAware {
	/**
	 * <code>messageSource</code> - MessageSource对象.
	 */
	private static MessageSource s_messageSource;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.context.MessageSourceAware#setMessageSource(org.
	 * springframework.context.MessageSource)
	 */
	public void setMessageSource(final MessageSource messageSource) {
		if (this.s_messageSource == null) {
			this.s_messageSource = messageSource;
		}

	}

	/**
	 * 获取国际化信息.
	 * 
	 * @param key
	 *            国际化的KEY
	 * @param args
	 *            国际化KEY对应的值中需替换的参数
	 * @return 国际化KEY对应的值
	 */
	public static String getMessage(final String key, final Object[] args) {
		if (key != null) {
			try {
				return s_messageSource.getMessage(key, args, I18nUtils.getCurrentLocale());
			} catch (NoSuchMessageException t_e) {
				return key;
			}
		}
		return null;
	}

	/**
	 * 获取国际化信息.
	 * 
	 * @param key
	 *            国际化的KEY
	 * @return 国际化KEY对应的值
	 */
	public static String getMessage(final String key) {
		return getMessage(key, null);
	}

}
