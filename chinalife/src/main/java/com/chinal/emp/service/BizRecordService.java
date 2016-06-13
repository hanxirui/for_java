package com.chinal.emp.service;

import java.util.List;

import org.durcframework.core.service.CrudService;
import org.springframework.stereotype.Service;

import com.chinal.emp.dao.BizRecordDao;
import com.chinal.emp.entity.BizRecord;

@Service
public class BizRecordService extends CrudService<BizRecord, BizRecordDao> {
	public List<BizRecord> getTongjiInfo(String currMonth) {
		return this.getDao().getTongjiInfo(currMonth);

	}
}