package permission.dao;

import org.apache.ibatis.annotations.Param;
import org.durcframework.core.dao.BaseDao;

import permission.entity.RGroupUser;

public interface RGroupUserDao extends BaseDao<RGroupUser> {
	void addGroupUser(AddGroupUserPojo addGroupUserPojo);
	void delByGroupId(@Param("groupId")int groupId);
}