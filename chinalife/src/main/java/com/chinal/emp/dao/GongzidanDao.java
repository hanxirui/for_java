package com.chinal.emp.dao;

import org.durcframework.core.dao.BaseDao;

import com.chinal.emp.entity.Gongzidan;

public interface GongzidanDao extends BaseDao<Gongzidan> {
	public void delByRiqi(String riqi);
}