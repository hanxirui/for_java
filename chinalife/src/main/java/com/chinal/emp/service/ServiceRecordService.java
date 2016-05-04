package com.chinal.emp.service;

import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.service.CrudService;
import org.springframework.stereotype.Service;

import com.chinal.emp.dao.ServiceRecordDao;
import com.chinal.emp.entity.ServiceRecord;

@Service
public class ServiceRecordService extends CrudService<ServiceRecord, ServiceRecordDao> {
	public Integer findServiceCount(ExpressionQuery query) {
		return this.getDao().findServiceCount(query);
	}
}