package com.ez.framework.core.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.ez.framework.core.service.ServiceException;

public class CommonExceptionResolver implements HandlerExceptionResolver {
	private final static Logger S_LOG = Logger
			.getLogger(CommonExceptionResolver.class);


	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		response.setStatus(400);
		response.setCharacterEncoding("UTF-8");
		ModelAndView view = new ModelAndView();
		view.setViewName("400");
		if (ex instanceof ServiceException) {
			view.getModel().put("errorMsg", ex.getMessage());
		} else if (ex instanceof InfoException) {
			view.getModel().put("errorMsg", ex.getMessage());
		} else {
			S_LOG.error(ex.getMessage(),ex);
			view.getModel().put("errorMsg", "对不起，服务器出现问题，请稍后访问。");
		}
		return view;
	}

}
