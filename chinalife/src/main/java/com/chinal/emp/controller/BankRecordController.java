package com.chinal.emp.controller;

import org.durcframework.core.support.BsgridController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.chinal.emp.entity.BankRecord;
import com.chinal.emp.entity.BankRecordSch;
import com.chinal.emp.service.BankRecordService;


@Controller
public class BankRecordController extends
		BsgridController<BankRecord, BankRecordService> {
        
    @RequestMapping("/openBankRecord.do")
	public String openBankRecord() {
		return "bankRecord";
	}
    
	@RequestMapping("/addBankRecord.do")
	public ModelAndView addBankRecord(BankRecord entity) {
		return this.add(entity);
	}

	@RequestMapping("/listBankRecord.do")
	public ModelAndView listBankRecord(BankRecordSch searchEntity) {
		return this.list(searchEntity);
	}

	@RequestMapping("/updateBankRecord.do")
	public ModelAndView updateBankRecord(BankRecord entity) {
		return this.modify(entity);
	}

	@RequestMapping("/delBankRecord.do")
	public ModelAndView delBankRecord(BankRecord entity) {
		return this.remove(entity);
	}
	
}
