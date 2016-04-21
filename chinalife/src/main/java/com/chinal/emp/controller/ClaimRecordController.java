package com.chinal.emp.controller;

import org.durcframework.core.support.BsgridController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.chinal.emp.entity.ClaimRecord;
import com.chinal.emp.entity.ClaimRecordSch;
import com.chinal.emp.service.ClaimRecordService;

@Controller
public class ClaimRecordController extends BsgridController<ClaimRecord, ClaimRecordService> {

	@RequestMapping("/openClaimRecord.do")
	public String openClaimRecord() {
		return "claimRecord";
	}

	@RequestMapping("/addClaimRecord.do")
	public ModelAndView addClaimRecord(ClaimRecord entity) {
		return this.add(entity);
	}

	@RequestMapping("/listClaimRecord.do")
	public ModelAndView listClaimRecord(ClaimRecordSch searchEntity) {
		return this.list(searchEntity);
	}

	@RequestMapping("/updateClaimRecord.do")
	public ModelAndView updateClaimRecord(ClaimRecord entity) {
		return this.modify(entity);
	}

	@RequestMapping("/delClaimRecord.do")
	public ModelAndView delClaimRecord(ClaimRecord entity) {
		return this.remove(entity);
	}

}
