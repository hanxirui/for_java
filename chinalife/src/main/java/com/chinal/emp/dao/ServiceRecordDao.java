package com.chinal.emp.dao;

import org.durcframework.core.dao.BaseDao;
import org.durcframework.core.expression.ExpressionQuery;

import com.chinal.emp.entity.ServiceRecord;

public interface ServiceRecordDao extends BaseDao<ServiceRecord> {
	Integer findServiceCount(ExpressionQuery query);
}