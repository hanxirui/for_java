package permission.dao;

import org.apache.ibatis.annotations.Param;
import org.durcframework.core.dao.BaseDao;

import permission.entity.RUserRole;
import permission.entity.UserRoleParam;

public interface RUserRoleDao extends BaseDao<RUserRole> {
	void delAllUserRole(UserRoleParam userRoleParam);
	void delGroupRole(RUserRole userRole);
	void setUserRole(UserRoleParam userRoleParam);
	void delByRoleId(@Param("roleId")int roleId);
}