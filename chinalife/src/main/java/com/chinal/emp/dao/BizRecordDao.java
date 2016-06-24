package com.chinal.emp.dao;

import java.util.List;

import org.durcframework.core.dao.BaseDao;

import com.chinal.emp.entity.BizRecord;

public interface BizRecordDao extends BaseDao<BizRecord> {

	List<BizRecord> getTongjiInfo(String startDate, String endDate, String title);
}