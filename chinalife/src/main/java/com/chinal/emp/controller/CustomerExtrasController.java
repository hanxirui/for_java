package com.chinal.emp.controller;

import org.durcframework.core.support.BsgridController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.chinal.emp.entity.CustomerExtras;
import com.chinal.emp.entity.CustomerExtrasSch;
import com.chinal.emp.service.CustomerExtrasService;

@Controller
public class CustomerExtrasController extends BsgridController<CustomerExtras, CustomerExtrasService> {

	@RequestMapping("/openCustomerExtras.do")
	public String openCustomerExtras() {
		return "customerExtras";
	}

	@RequestMapping("/addCustomerExtras.do")
	public ModelAndView addCustomerExtras(CustomerExtras entity) {
		return this.add(entity);
	}

	@RequestMapping("/listCustomerExtras.do")
	public ModelAndView listCustomerExtras(CustomerExtrasSch searchEntity) {
		return this.list(searchEntity);
	}

	@RequestMapping("/updateCustomerExtras.do")
	public ModelAndView updateCustomerExtras(CustomerExtras entity) {
		return this.modify(entity);
	}

	@RequestMapping("/delCustomerExtras.do")
	public ModelAndView delCustomerExtras(CustomerExtras entity) {
		return this.remove(entity);
	}

}
