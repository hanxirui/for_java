package permission.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.durcframework.core.dao.BaseDao;

import permission.entity.RSysRes;

public interface RSysResDao extends BaseDao<RSysRes> {
	List<RSysRes> findUserMenu(@Param("userId") int userId);
}