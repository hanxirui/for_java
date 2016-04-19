package com.riil.lightning.sys.dao;

import com.riil.lightning.common.persistence.CrudDao;
import com.riil.lightning.common.persistence.annotation.MyBatisDao;
import com.riil.lightning.sys.entity.Log;

/**
 * 日志DAO接口
 * @author ThinkGem
 * @version 2014-05-16
 */
@MyBatisDao
public interface LogDao extends CrudDao<Log> {

}
