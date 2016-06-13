package com.chinal.emp.controller;

import org.durcframework.core.support.BsgridController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.chinal.emp.entity.Loginrecord;
import com.chinal.emp.entity.LoginrecordSch;
import com.chinal.emp.service.LoginrecordService;

@Controller
public class LoginrecordController extends BsgridController<Loginrecord, LoginrecordService> {

	@RequestMapping("/openLoginrecord.do")
	public String openLoginrecord() {
		return "loginrecord";
	}

	@RequestMapping("/addLoginrecord.do")
	public ModelAndView addLoginrecord(Loginrecord entity) {
		return this.add(entity);
	}

	@RequestMapping("/listLoginrecord.do")
	public ModelAndView listLoginrecord(LoginrecordSch searchEntity) {
		return this.list(searchEntity);
	}

	@RequestMapping("/updateLoginrecord.do")
	public ModelAndView updateLoginrecord(Loginrecord entity) {
		return this.modify(entity);
	}

	@RequestMapping("/delLoginrecord.do")
	public ModelAndView delLoginrecord(Loginrecord entity) {
		return this.remove(entity);
	}

}
