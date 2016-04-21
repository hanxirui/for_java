package com.chinal.emp.service;

import org.durcframework.core.service.CrudService;
import com.chinal.emp.dao.ServiceRecordDao;
import com.chinal.emp.entity.ServiceRecord;
import org.springframework.stereotype.Service;

@Service
public class ServiceRecordService extends CrudService<ServiceRecord, ServiceRecordDao> {

}