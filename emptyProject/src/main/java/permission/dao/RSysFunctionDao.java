package permission.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.durcframework.core.dao.BaseDao;

import permission.entity.RSysFunction;

public interface RSysFunctionDao extends BaseDao<RSysFunction> {
	/**
	 * 获取用户系统功能
	 * @param username
	 * @return
	 */
	List<RSysFunction> findUserSysFunction(@Param("userId")int userId);
}