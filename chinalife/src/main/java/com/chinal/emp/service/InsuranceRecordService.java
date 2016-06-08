package com.chinal.emp.service;

import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.service.CrudService;
import org.springframework.stereotype.Service;

import com.chinal.emp.dao.InsuranceRecordDao;
import com.chinal.emp.entity.InsuranceRecord;

@Service
public class InsuranceRecordService extends CrudService<InsuranceRecord, InsuranceRecordDao> {
	public Integer findInsuranceCount(ExpressionQuery query) {
		return this.getDao().findInsuranceCount(query);
	}

	public void delInsurances(String[] ids) {
		this.getDao().delInsuranceByids(ids);
	}
}