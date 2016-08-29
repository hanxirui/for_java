package com.stone.lightning.sys.dao;

import com.stone.lightning.common.persistence.CrudDao;
import com.stone.lightning.common.persistence.annotation.MyBatisDao;
import com.stone.lightning.sys.entity.Log;

/**
 * 日志DAO接口
 * @author ThinkGem
 * @version 2014-05-16
 */
@MyBatisDao
public interface LogDao extends CrudDao<Log> {

}
