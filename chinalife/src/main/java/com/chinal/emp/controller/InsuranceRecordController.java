package com.chinal.emp.controller;

import org.durcframework.core.support.BsgridController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.chinal.emp.entity.CustomerBasic;
import com.chinal.emp.entity.InsuranceRecord;
import com.chinal.emp.entity.InsuranceRecordSch;
import com.chinal.emp.service.CustomerBasicService;
import com.chinal.emp.service.InsuranceRecordService;

@Controller
public class InsuranceRecordController extends BsgridController<InsuranceRecord, InsuranceRecordService> {
	@Autowired
	private CustomerBasicService customerBasicService;

	@RequestMapping("/openInsuranceForC.do")
	public ModelAndView openInsuranceForC(String id) {
		ModelAndView mv = new ModelAndView();
		CustomerBasic cus = customerBasicService.get(Integer.parseInt(id));
		mv.addObject("customer", cus);
		mv.setViewName("insuranceRecordForC");
		return mv;
	}

	@RequestMapping("/openInsuranceRecord.do")
	public String openInsuranceRecord() {
		return "insuranceRecord";
	}

	@RequestMapping("/addInsuranceRecord.do")
	public ModelAndView addInsuranceRecord(InsuranceRecord entity) {
		return this.add(entity);
	}

	@RequestMapping("/listInsuranceRecord.do")
	public ModelAndView listInsuranceRecord(InsuranceRecordSch searchEntity, String idcardnum) {
		if (idcardnum != null && !"".equals(idcardnum)) {
			searchEntity.setCustomerIdcardnum(idcardnum);
		}
		return this.list(searchEntity);
	}

	@RequestMapping("/updateInsuranceRecord.do")
	public ModelAndView updateInsuranceRecord(InsuranceRecord entity) {
		return this.modify(entity);
	}

	@RequestMapping("/delInsuranceRecord.do")
	public ModelAndView delInsuranceRecord(InsuranceRecord entity) {
		return this.remove(entity);
	}

}
