package com.chinal.emp.service;

import java.util.List;

import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.service.CrudService;
import org.springframework.stereotype.Service;

import com.chinal.emp.dao.InsuranceRecordDao;
import com.chinal.emp.entity.InsuranceRecord;
import com.chinal.emp.report.vo.InsurTrendReportVo;

@Service
public class InsuranceRecordService extends CrudService<InsuranceRecord, InsuranceRecordDao> {
	public Integer findInsuranceCount(ExpressionQuery query) {
		return this.getDao().findInsuranceCount(query);
	}

	public void delById(int id) {
		this.getDao().delById(id);
	}

	public void updateByToubaoren(InsuranceRecord record) {

		this.getDao().updateByToubaoren(record);
	}

	public List<InsurTrendReportVo> queryInsurTrendReport(String startTime, String endTime) {
		return this.getDao().queryInsurTrendReport(startTime, endTime);
	}
}