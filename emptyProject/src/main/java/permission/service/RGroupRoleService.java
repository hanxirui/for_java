package permission.service;

import java.util.List;

import org.durcframework.core.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import permission.constant.RoleType;
import permission.dao.RGroupRoleDao;
import permission.entity.RGroupRole;
import permission.entity.RGroupUser;
import permission.entity.RRole;
import permission.entity.RUserRole;

@Service
public class RGroupRoleService extends CrudService<RGroupRole, RGroupRoleDao> implements IRole,IGroup {

	@Autowired
	private RRoleService roleService;
	@Autowired
	private RUserRoleService userRoleService;
	@Autowired
	private RGroupUserService groupUserService;
	
	/**
	 * 获取用户组对应的角色ID
	 * @param groupId
	 * @return
	 */
	public List<Integer> getRoleIdsByGroupId(int groupId) {
		return this.getDao().getRoleIdsByGroupId(groupId);
	}
	
	/**
	 * 添加用户组角色
	 * 1. 添加角色表数据
	 * 2. 添加用户组角色表数据
	 * 3. 添加用户角色表数据
	 * @param groupId
	 * @param roleName
	 */
	public void addGroupRole(int groupId,String roleName) {
		// 1
		RRole role = new RRole();
		role.setRoleName(roleName);
		role.setRoleType(RoleType.GROUP);
		roleService.save(role);
		// 2
		RGroupRole groupRole = new RGroupRole();
		groupRole.setRoleId(role.getRoleId());
		groupRole.setGroupId(groupId);
		
		this.save(groupRole);
		// 3
		this.setGroupUserRole(groupId, role.getRoleId());
	}
	
	/**
	 * 添加用户角色
	 * @param groupId
	 * @param roleId
	 */
	public void setGroupUserRole(int groupId,int roleId) {
		List<RGroupUser> groupUsers = groupUserService.getGroupUserByGroupId(groupId);
		
		for (RGroupUser groupUser : groupUsers) {
			RUserRole userRole = new RUserRole();
			userRole.setRoleId(roleId);
			userRole.setRoleType(RoleType.GROUP);
			userRole.setUserId(groupUser.getUserId());
			userRoleService.save(userRole);
		}
	}
	
	/**
	 * 删除用户组角色
	 */
	@Override
	public int del(RGroupRole groupRole) {
		RRole role = new RRole();
		role.setRoleId(groupRole.getRoleId());
		return roleService.del(role);
	}

	@Override
	public void delByRoleId(int roleId) {
		this.getDao().delByRoleId(roleId);
	}

	@Override
	public void delByGroupId(int groupId) {
		List<Integer> roleIds = this.getRoleIdsByGroupId(groupId);
		RGroupRole groupRole = new RGroupRole();
		for (Integer roleId : roleIds) {
			groupRole.setRoleId(roleId);
			this.del(groupRole);
		}
	}
	
	
}