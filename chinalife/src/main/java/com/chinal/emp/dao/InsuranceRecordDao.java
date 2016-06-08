package com.chinal.emp.dao;

import org.durcframework.core.dao.BaseDao;
import org.durcframework.core.expression.ExpressionQuery;

import com.chinal.emp.entity.InsuranceRecord;

public interface InsuranceRecordDao extends BaseDao<InsuranceRecord> {
	Integer findInsuranceCount(ExpressionQuery query);

	void delInsuranceByids(String[] ids);
}