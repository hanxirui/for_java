package permission.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.durcframework.core.dao.BaseDao;

import permission.entity.RGroupRole;

public interface RGroupRoleDao extends BaseDao<RGroupRole> {
	List<Integer> getRoleIdsByGroupId(int groupId);
	void delByRoleId(@Param("roleId") int roleId);
}