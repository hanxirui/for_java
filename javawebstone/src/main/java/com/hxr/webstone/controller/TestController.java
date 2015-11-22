package com.hxr.webstone.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ez.framework.core.servlet.BaseDispatcherServlet;

@RestController
@RequestMapping("/test")
public class TestController extends BaseController {
	@RequestMapping("/index")
	public ModelAndView index() throws Exception {
		ModelAndView t_view = BaseDispatcherServlet.getModeAndView();

		t_view.setViewName("/test/index");

		return t_view;
	}
}
