package com.chinal.emp.dao;

import java.util.List;

import org.durcframework.core.dao.BaseDao;
import org.durcframework.core.expression.ExpressionQuery;

import com.chinal.emp.entity.InsuranceRecord;
import com.chinal.emp.report.vo.InsurTrendReportVo;

public interface InsuranceRecordDao extends BaseDao<InsuranceRecord> {
	Integer findInsuranceCount(ExpressionQuery query);

	void delInsuranceByids(String[] ids);

	void updateByToubaoren(InsuranceRecord record);

	List<InsurTrendReportVo> queryInsurTrendReport(String startTime, String endTime);
}