package permission.service;

import java.util.ArrayList;
import java.util.List;

import org.durcframework.core.expression.Expression;
import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.subexpression.InnerJoinExpression;
import org.durcframework.core.expression.subexpression.ValueExpression;
import org.durcframework.core.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import permission.dao.RRoleDao;
import permission.entity.RDataPermission;
import permission.entity.RRole;
import permission.entity.RSysFunction;

@Service
public class RRoleService extends CrudService<RRole, RRoleDao> implements IRole {
	
	@Autowired
	private RRolePermissionService rolePermissionService;
	@Autowired
	private RUserRoleService userRoleService;
	@Autowired
	private RGroupRoleService groupRoleService;
	
	
	public void addRole(RRole role,List<Integer> sfIds) {
		this.save(role);
		
		if(role.getRoleId() > 0) {
			rolePermissionService.setSysFunctionRole(sfIds, role.getRoleId());
		}
	}
	
	public void updateRole(RRole role,List<Integer> sfIds){
		this.update(role);
		
		if(role.getRoleId() > 0) {
			rolePermissionService.setSysFunctionRole(sfIds, role.getRoleId());
		}
	}

	/**
	 * 返回系统功能分配的角色
	 * @param sysFunction
	 * @return
	 */
	public List<RRole> getRolesBySysFunction(RSysFunction sysFunction){
		return this.getDao().findRoleByFunction(sysFunction.getSfId());
	}
	
	/**
	 * 删除角色
	 * 先删除关联表信息,再删除自身
	 * role,userRole,rolePerm,groupRole
	 */
	@Override
	public int del(RRole entity) {
		int roleId = entity.getRoleId();
		// 删除用户角色
		userRoleService.delByRoleId(roleId);
		// 删除角色权限
		rolePermissionService.delByRoleId(roleId);
		// 删除用户组
		groupRoleService.delByRoleId(roleId);
		
		this.delByRoleId(roleId);
		
		return 0;
	}
	
	
	@Override
	public void delByRoleId(int roleId) {
		RRole role = new RRole();
		role.setRoleId(roleId);
		this.getDao().del(role);
	}

	/**
	 * 获取用户数据权限对应的角色
	 * @param dpId 
	 * @return
	 */
	public List<RRole> getDataPermissionRole(RDataPermission entity){
		ExpressionQuery query = ExpressionQuery.buildQueryAll();
		
		query.add(new InnerJoinExpression("r_data_permission_role", "t2", "role_id", "role_id"));
		
		query.add(new ValueExpression("t2.dp_id", entity.getDpId()));
		
		return this.find(query);
	}
	
	
	/**
	 * 获取用户的角色
	 * @param userId
	 * @return
	 */
	public List<RRole> getUserRole(int userId){
		List<Expression> expres = new ArrayList<Expression>();
		expres.add(new InnerJoinExpression("r_user_role", "t2", "role_id", "role_id"));
		expres.add(new ValueExpression("t2.user_id", userId));
		
		ExpressionQuery query = ExpressionQuery.buildQueryAll();
		query.addAll(expres);
		
		return this.find(query);
	}
	
	/**
	 * 获取用户组角色
	 * @param groupId
	 * @return
	 */
	public List<RRole> getRolesByGroupId(int groupId) {
		return this.getDao().getRolesByGroupId(groupId);
	}
	
}
