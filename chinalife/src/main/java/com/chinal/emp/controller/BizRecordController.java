package com.chinal.emp.controller;

import org.durcframework.core.support.BsgridController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.chinal.emp.entity.BizRecord;
import com.chinal.emp.entity.BizRecordSch;
import com.chinal.emp.service.BizRecordService;


@Controller
public class BizRecordController extends
		BsgridController<BizRecord, BizRecordService> {
        
    @RequestMapping("/openBizRecord.do")
	public String openBizRecord() {
		return "bizRecord";
	}
    
	@RequestMapping("/addBizRecord.do")
	public ModelAndView addBizRecord(BizRecord entity) {
		return this.add(entity);
	}

	@RequestMapping("/listBizRecord.do")
	public ModelAndView listBizRecord(BizRecordSch searchEntity) {
		return this.list(searchEntity);
	}

	@RequestMapping("/updateBizRecord.do")
	public ModelAndView updateBizRecord(BizRecord entity) {
		return this.modify(entity);
	}

	@RequestMapping("/delBizRecord.do")
	public ModelAndView delBizRecord(BizRecord entity) {
		return this.remove(entity);
	}
	
}
