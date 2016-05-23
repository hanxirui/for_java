package com.chinal.emp.service;

import org.durcframework.core.service.CrudService;
import com.chinal.emp.dao.BizRecordDao;
import com.chinal.emp.entity.BizRecord;
import org.springframework.stereotype.Service;

@Service
public class BizRecordService extends CrudService<BizRecord, BizRecordDao> {

}