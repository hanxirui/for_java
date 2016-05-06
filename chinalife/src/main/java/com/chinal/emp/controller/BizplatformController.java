package com.chinal.emp.controller;

import org.durcframework.core.support.BsgridController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.chinal.emp.entity.Bizplatform;
import com.chinal.emp.entity.BizplatformSch;
import com.chinal.emp.service.BizplatformService;

@Controller
public class BizplatformController extends BsgridController<Bizplatform, BizplatformService> {

	@RequestMapping("/openBizplatform.do")
	public String openBizplatform() {
		return "bizplatform";
	}

	@RequestMapping("/openBizCalendar.do")
	public String getBizEvents() {
		return "bizCalendar";
	}

	@RequestMapping("/addBizplatform.do")
	public ModelAndView addBizplatform(Bizplatform entity) {
		return this.add(entity);
	}

	@RequestMapping("/listBizplatform.do")
	public ModelAndView listBizplatform(BizplatformSch searchEntity) {
		return this.list(searchEntity);
	}

	@RequestMapping("/updateBizplatform.do")
	public ModelAndView updateBizplatform(Bizplatform entity) {
		return this.modify(entity);
	}

	@RequestMapping("/delBizplatform.do")
	public ModelAndView delBizplatform(Bizplatform entity) {
		return this.remove(entity);
	}

}
