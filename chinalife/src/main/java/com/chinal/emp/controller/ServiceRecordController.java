package com.chinal.emp.controller;

import org.durcframework.core.support.BsgridController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.chinal.emp.entity.CustomerBasic;
import com.chinal.emp.entity.ServiceRecord;
import com.chinal.emp.entity.ServiceRecordSch;
import com.chinal.emp.service.CustomerBasicService;
import com.chinal.emp.service.ServiceRecordService;

@Controller
public class ServiceRecordController extends BsgridController<ServiceRecord, ServiceRecordService> {
	@Autowired
	private CustomerBasicService customerBasicService;

	@RequestMapping("/openServiceRecordForC.do")
	public ModelAndView openServiceRecordForC(String id) {
		ModelAndView mv = new ModelAndView();
		CustomerBasic cus = customerBasicService.get(Integer.parseInt(id));
		mv.addObject("customer", cus);
		mv.setViewName("serviceRecordForC");
		return mv;
	}

	@RequestMapping("/openServiceRecord.do")
	public String openServiceRecord() {
		return "serviceRecord";
	}

	@RequestMapping("/addServiceRecord.do")
	public ModelAndView addServiceRecord(ServiceRecord entity) {
		return this.add(entity);
	}

	@RequestMapping("/listServiceRecord.do")
	public ModelAndView listServiceRecord(ServiceRecordSch searchEntity, String idcardnum) {
		if (idcardnum != null && !"".equals(idcardnum)) {
			searchEntity.setIdcardnum(idcardnum);
		}
		return this.list(searchEntity);
	}

	@RequestMapping("/updateServiceRecord.do")
	public ModelAndView updateServiceRecord(ServiceRecord entity) {
		return this.modify(entity);
	}

	@RequestMapping("/delServiceRecord.do")
	public ModelAndView delServiceRecord(ServiceRecord entity) {
		return this.remove(entity);
	}

}
