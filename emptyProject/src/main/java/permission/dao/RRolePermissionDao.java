package permission.dao;

import org.apache.ibatis.annotations.Param;
import org.durcframework.core.dao.BaseDao;

import permission.entity.FunctionRoleParam;
import permission.entity.RRolePermission;

public interface RRolePermissionDao extends BaseDao<RRolePermission> {
	void delBySfId(@Param("sfId") int sfId);
	void delByRoleId(@Param("roleId") int roleId);
	void setFunctionRole(FunctionRoleParam functionRoleParam);
}