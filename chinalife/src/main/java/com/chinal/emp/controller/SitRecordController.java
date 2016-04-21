package com.chinal.emp.controller;

import org.durcframework.core.support.BsgridController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.chinal.emp.entity.SitRecord;
import com.chinal.emp.entity.SitRecordSch;
import com.chinal.emp.service.SitRecordService;

@Controller
public class SitRecordController extends BsgridController<SitRecord, SitRecordService> {

	@RequestMapping("/openSitRecord.do")
	public String openSitRecord() {
		return "sitRecord";
	}

	@RequestMapping("/addSitRecord.do")
	public ModelAndView addSitRecord(SitRecord entity) {
		return this.add(entity);
	}

	@RequestMapping("/listSitRecord.do")
	public ModelAndView listSitRecord(SitRecordSch searchEntity) {
		return this.list(searchEntity);
	}

	@RequestMapping("/updateSitRecord.do")
	public ModelAndView updateSitRecord(SitRecord entity) {
		return this.modify(entity);
	}

	@RequestMapping("/delSitRecord.do")
	public ModelAndView delSitRecord(SitRecord entity) {
		return this.remove(entity);
	}

}
