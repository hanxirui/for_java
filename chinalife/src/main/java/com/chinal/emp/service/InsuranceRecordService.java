package com.chinal.emp.service;

import org.durcframework.core.service.CrudService;
import com.chinal.emp.dao.InsuranceRecordDao;
import com.chinal.emp.entity.InsuranceRecord;
import org.springframework.stereotype.Service;

@Service
public class InsuranceRecordService extends CrudService<InsuranceRecord, InsuranceRecordDao> {

}