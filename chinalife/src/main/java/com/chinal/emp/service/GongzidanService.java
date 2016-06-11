package com.chinal.emp.service;

import org.durcframework.core.service.CrudService;
import org.springframework.stereotype.Service;

import com.chinal.emp.dao.GongzidanDao;
import com.chinal.emp.entity.Gongzidan;

@Service
public class GongzidanService extends CrudService<Gongzidan, GongzidanDao> {
	public void delByRiqi(String riqi) {
		this.getDao().delByRiqi(riqi);
	}
}