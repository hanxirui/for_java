package com.chinal.emp.service;

import org.durcframework.core.service.CrudService;
import com.chinal.emp.dao.BankRecordDao;
import com.chinal.emp.entity.BankRecord;
import org.springframework.stereotype.Service;

@Service
public class BankRecordService extends CrudService<BankRecord, BankRecordDao> {

}