package com.chinal.emp.controller;

import org.durcframework.core.support.BsgridController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.chinal.emp.entity.CustomerBasic;
import com.chinal.emp.entity.CustomerBasicSch;
import com.chinal.emp.service.CustomerBasicService;


@Controller
public class CustomerBasicController extends
		BsgridController<CustomerBasic, CustomerBasicService> {
        
    @RequestMapping("/openCustomerBasic.do")
	public String openCustomerBasic() {
		return "customerBasic";
	}
    
	@RequestMapping("/addCustomerBasic.do")
	public ModelAndView addCustomerBasic(CustomerBasic entity) {
		return this.add(entity);
	}

	@RequestMapping("/listCustomerBasic.do")
	public ModelAndView listCustomerBasic(CustomerBasicSch searchEntity) {
		return this.list(searchEntity);
	}

	@RequestMapping("/updateCustomerBasic.do")
	public ModelAndView updateCustomerBasic(CustomerBasic entity) {
		return this.modify(entity);
	}

	@RequestMapping("/delCustomerBasic.do")
	public ModelAndView delCustomerBasic(CustomerBasic entity) {
		return this.remove(entity);
	}
	
}
