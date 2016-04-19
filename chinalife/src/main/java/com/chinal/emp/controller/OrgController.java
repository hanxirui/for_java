package com.chinal.emp.controller;

import org.durcframework.core.support.BsgridController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.chinal.emp.entity.Org;
import com.chinal.emp.entity.OrgSch;
import com.chinal.emp.service.OrgService;

@Controller
public class OrgController extends BsgridController<Org, OrgService> {

	@RequestMapping("/addOrg.do")
	public ModelAndView addOrg(Org entity) {
		return this.add(entity);
	}

	@RequestMapping("/listOrg.do")
	public ModelAndView listOrg(OrgSch searchEntity) {
		return this.list(searchEntity);
	}

	@RequestMapping("/updateOrg.do")
	public ModelAndView updateOrg(Org entity) {
		return this.modify(entity);
	}

	@RequestMapping("/delOrg.do")
	public ModelAndView delOrg(Org entity) {
		return this.remove(entity);
	}

}
