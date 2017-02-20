package permission.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.durcframework.core.dao.BaseDao;

import permission.entity.RRole;

public interface RRoleDao extends BaseDao<RRole> {
	List<RRole> findRoleByFunction(@Param("sfId") int sfId);
	List<RRole> getRolesByGroupId(@Param("groupId") int groupId);
}