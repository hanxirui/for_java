package app.service;

import org.durcframework.core.service.CrudService;
import app.dao.OrderInfoDao;
import app.entity.OrderInfo;
import org.springframework.stereotype.Service;

@Service
public class OrderInfoService extends CrudService<OrderInfo, OrderInfoDao> {

}