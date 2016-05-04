package com.chinal.emp.service;

import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.service.CrudService;
import org.springframework.stereotype.Service;

import com.chinal.emp.dao.SitRecordDao;
import com.chinal.emp.entity.SitRecord;

@Service
public class SitRecordService extends CrudService<SitRecord, SitRecordDao> {
	public Integer findVisitCount(ExpressionQuery query) {
		return this.getDao().findVisitCount(query);
	}
}