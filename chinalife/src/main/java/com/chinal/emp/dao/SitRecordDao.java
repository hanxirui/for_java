package com.chinal.emp.dao;

import org.durcframework.core.dao.BaseDao;
import org.durcframework.core.expression.ExpressionQuery;

import com.chinal.emp.entity.SitRecord;

public interface SitRecordDao extends BaseDao<SitRecord> {
	Integer findVisitCount(ExpressionQuery query);
}