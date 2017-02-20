package permission.service;

import java.util.Collections;
import java.util.List;

import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.subexpression.ValueExpression;
import org.durcframework.core.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import permission.constant.RoleType;
import permission.dao.RUserRoleDao;
import permission.entity.RUserRole;
import permission.entity.SetUserRoleParam;

@Service
public class RUserRoleService extends CrudService<RUserRole, RUserRoleDao> implements IRole {
	
	@Autowired
	private RGroupUserService groupUserService;
	
	/**
	 * 设置用户角色
	 */
	public void setUserRole(SetUserRoleParam userRole) {
		if (userRole.getUserId() == 0) {
			return;
		}
		
		// 添加用户组角色
		if(userRole.getRoleType() == RoleType.GROUP) {
			this.addGroupRole(userRole);
		}else if(userRole.getRoleType() == RoleType.PERSON) {
			this.addPersonRole(userRole);
		}
	}
	
	
	public void addPersonRole(RUserRole userRole) {
		if(userRole.getRoleType() == RoleType.PERSON) {
			this.save(userRole);
		}
	}
	
	public void addGroupRole(SetUserRoleParam userRole) {
		if(userRole.getRoleType() == RoleType.GROUP) {
			groupUserService.addUserGroup(userRole);
		}
	}
	
	/**
	 * 获取用户角色
	 * @param userId
	 * @return
	 */
	public List<RUserRole> getUserRole(int userId) {
		if(userId == 0){
			return Collections.emptyList();
		}
		ExpressionQuery query = ExpressionQuery.buildQueryAll();
		query.add(new ValueExpression("t.user_id", userId));
		
		return this.find(query);
	}
	
	public List<RUserRole> getUserRoleByRoleId(int roleId){
		ExpressionQuery query = ExpressionQuery.buildQueryAll();
		query.add(new ValueExpression("t.role_id", roleId));
		return this.find(query);
	}
	
	@Override
	public void delByRoleId(int roleId){
		this.getDao().delByRoleId(roleId);
	}
	
}
