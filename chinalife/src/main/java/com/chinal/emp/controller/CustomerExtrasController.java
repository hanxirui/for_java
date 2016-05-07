package com.chinal.emp.controller;

import org.durcframework.core.support.BsgridController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.chinal.emp.entity.CustomerBasic;
import com.chinal.emp.entity.CustomerExtras;
import com.chinal.emp.entity.CustomerExtrasSch;
import com.chinal.emp.service.CustomerBasicService;
import com.chinal.emp.service.CustomerExtrasService;

@Controller
public class CustomerExtrasController extends BsgridController<CustomerExtras, CustomerExtrasService> {

	@Autowired
	private CustomerBasicService customerBasicService;

	@RequestMapping("/openCustomerForC.do")
	public ModelAndView openCustomerForC(String id) {
		ModelAndView mv = new ModelAndView();
		CustomerBasic cus = customerBasicService.get(Integer.parseInt(id));
		mv.addObject("customer", cus);
		mv.setViewName("customerExtrasForC");
		return mv;
	}

	@RequestMapping("/openCustomerExtras.do")
	public String openCustomerExtras() {
		return "customerExtras";
	}

	@RequestMapping("/addCustomerExtras.do")
	public ModelAndView addCustomerExtras(CustomerExtras entity) {
		return this.add(entity);
	}

	@RequestMapping("/listCustomerExtras.do")
	public ModelAndView listCustomerExtras(CustomerExtrasSch searchEntity, String idcardnum) {
		if (idcardnum != null && !"".equals(idcardnum)) {
			searchEntity.setIdcardnum(idcardnum);
		}
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
