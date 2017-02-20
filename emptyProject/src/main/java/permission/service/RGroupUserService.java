package permission.service;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.QBC;
import org.durcframework.core.expression.subexpression.ListExpression;
import org.durcframework.core.expression.subexpression.ValueExpression;
import org.durcframework.core.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import permission.constant.RoleType;
import permission.dao.AddGroupUserPojo;
import permission.dao.RGroupUserDao;
import permission.entity.RGroup;
import permission.entity.RGroupUser;
import permission.entity.RUserRole;
import permission.entity.SetUserRoleParam;

@Service
public class RGroupUserService extends CrudService<RGroupUser, RGroupUserDao> implements IGroup{

	@Autowired
	private RGroupService groupService;
	@Autowired
	private RUserRoleService userRoleService;
	@Autowired
	private RGroupRoleService groupRoleService;
	
	/**
	 * 添加用户组成员
	 * 1. 添加用户组成员表数据
	 * 2. 添加用户组成员角色
	 * @param addGroupUserPojo
	 */
	public void addGroupUser(AddGroupUserPojo addGroupUserPojo) {
		
		int groupId = addGroupUserPojo.getGroupId();
		RGroup group = groupService.get(groupId);
		if(group == null) {
			return;
		}
		
		// 1
		this.getDao().addGroupUser(addGroupUserPojo);
		// 2
		List<Integer> roleIds = groupRoleService.getRoleIdsByGroupId(groupId);
		List<Integer> userIds = addGroupUserPojo.getUserIds();
		
		RUserRole userRoleParam = new RUserRole();
		for (int userId : userIds) {
			for (Integer roleId : roleIds) {
				userRoleParam.setRoleId(roleId);
				userRoleParam.setRoleType(RoleType.GROUP);
				userRoleParam.setUserId(userId);
				userRoleService.save(userRoleParam);
			}
		}
		
	}
	
	/**
	 * 通过用户角色来添加到用户组
	 * @param userRole
	 */
	public void addUserGroup(SetUserRoleParam userRoleParam) {
		AddGroupUserPojo addGroupUserPojo = new AddGroupUserPojo();
		
		addGroupUserPojo.setGroupId(userRoleParam.getGroupId());
		addGroupUserPojo.setUserIds(Arrays.asList(userRoleParam.getUserId()));
		
		this.addGroupUser(addGroupUserPojo);
	}
	
	public RGroupUser getByRoleId(int roleId) {
		QBC<RGroupUser> qbc = QBC.create(this.getDao());
		List<RGroupUser> list = qbc.eq("role_id", roleId).limit(1).list();
		if(CollectionUtils.isNotEmpty(list)) {
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 根据groupId获取用户组数据
	 * @param groupId
	 * @return
	 */
	public List<RGroupUser> getGroupUserByGroupId(int groupId) {
		QBC<RGroupUser> qbc = QBC.create(this.getDao());
		qbc.eq("group_id", groupId);
		return qbc.listAll();
	}
	
	/**
	 * 删除用户组成员
	 * 1. 删除用户组成员表数据
	 * 2. 删除用户角色表
	 * groupUser,userRole
	 */
	public void delGroupUser(RGroupUser entity) {
		this.del(entity); // 1
		
		List<Integer> roleIds = groupRoleService.getRoleIdsByGroupId(entity.getGroupId());
		ExpressionQuery query = new ExpressionQuery();
		query.add(new ListExpression("role_id", roleIds));
		query.add(new ValueExpression("user_id", entity.getUserId()));
		
		userRoleService.delByExpression(query); // 2
	}
	
	@Override
	public void delByGroupId(int groupId) {
		this.getDao().delByGroupId(groupId);
	}
	
	
}