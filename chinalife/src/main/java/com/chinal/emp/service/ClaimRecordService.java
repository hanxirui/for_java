package com.chinal.emp.service;

import org.durcframework.core.service.CrudService;
import com.chinal.emp.dao.ClaimRecordDao;
import com.chinal.emp.entity.ClaimRecord;
import org.springframework.stereotype.Service;

@Service
public class ClaimRecordService extends CrudService<ClaimRecord, ClaimRecordDao> {

}