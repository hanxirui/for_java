package com.riil.lightning.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.riil.lightning.common.persistence.BaseEntity;
import com.riil.lightning.common.persistence.CrudDao;
import com.riil.lightning.common.persistence.Page;
import com.riil.lightning.common.utils.IdGen;

/**
 * Service基类
 * @author ThinkGem
 * @version 2014-05-16
 */
@Transactional(readOnly = true)
public abstract class CrudService<D extends CrudDao<T>, T extends BaseEntity<T>> extends BaseService {
	
	/**
	 * 持久层对象
	 */
	@Autowired
	protected D dao;
	
	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	public T selectOne(String id) {
		return dao.selectOne(id);
	}
	
	/**
	 * 查询列表数据
	 * @param entity
	 * @return
	 */
	public List<T> findList(T entity) {
		return dao.findList(entity);
	}
	/**
	 * @return
	 */
	public List<T> findAllList(){
		return dao.findAllList();
	}
	
	/**
	 * 查询分页数据
	 * @param page 分页对象
	 * @param entity
	 * @return
	 */
	public Page<T> findPage(Page<T> page, T entity) {
		entity.setPage(page);
		page.setList(dao.findList(entity));
		return page;
	}

	/**
	 * 保存数据（插入或更新）
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public void save(T entity) {
		if (entity.getIsNewRecord()){
			entity.setId(IdGen.uuid());
			dao.insert(entity);
		}else{
			dao.update(entity);
		}
	}
	/**
	 * 删除数据
	 * @param id
	 */
	@Transactional(readOnly = false)
	public void delete(String id){
		dao.delete(id);
	}

}
