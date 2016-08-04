package com.chinal.emp.controller;

import org.durcframework.core.support.BsgridController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.chinal.emp.entity.CustomerBasic;
import com.chinal.emp.entity.Fenpeilishi;
import com.chinal.emp.entity.FenpeilishiSch;
import com.chinal.emp.service.CustomerBasicService;
import com.chinal.emp.service.FenpeilishiService;

@Controller
public class FenpeilishiController extends BsgridController<Fenpeilishi, FenpeilishiService> {
	@Autowired
	private CustomerBasicService customerBasicService;

	@RequestMapping("/openFenpeilishi.do")
	public ModelAndView openFenpeilishi(String id) {
		ModelAndView mv = new ModelAndView();
		CustomerBasic cus = customerBasicService.get(Integer.parseInt(id));
		mv.addObject("customer", cus);
		mv.setViewName("fenpeilishi");
		return mv;
	}

	@RequestMapping("/addFenpeilishi.do")
	public ModelAndView addFenpeilishi(Fenpeilishi entity) {
		return this.add(entity);
	}

	@RequestMapping("/listFenpeilishi.do")
	public ModelAndView listFenpeilishi(FenpeilishiSch searchEntity) {
		return this.list(searchEntity);
	}

	@RequestMapping("/updateFenpeilishi.do")
	public ModelAndView updateFenpeilishi(Fenpeilishi entity) {
		return this.modify(entity);
	}

	@RequestMapping("/delFenpeilishi.do")
	public ModelAndView delFenpeilishi(Fenpeilishi entity) {
		return this.remove(entity);
	}

}
