package permission.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.subexpression.ValueExpression;
import org.durcframework.core.service.CrudService;
import org.springframework.stereotype.Service;

import permission.dao.RRolePermissionDao;
import permission.entity.FunctionRoleParam;
import permission.entity.RRolePermission;

@Service
public class RRolePermissionService extends CrudService<RRolePermission, RRolePermissionDao> implements IRole {

	/**
	 * 根据功能查询角色权限
	 * @param sfId
	 * @return
	 */
	public List<RRolePermission> getRolePermissionBySfId(int sfId){
		ExpressionQuery query = ExpressionQuery.buildQueryAll();
		query.add(new ValueExpression("sf_id", sfId));
		return this.find(query);
	}
	
	/**
	 * 根据角色查询功能
	 * @param roleId
	 * @return
	 */
	public List<RRolePermission> getRolePermissionByRole(int roleId){
		ExpressionQuery query = ExpressionQuery.buildQueryAll();
		query.add(new ValueExpression("role_id", roleId));
		
		return this.find(query);
	}
	
	/**
	 * 批量设置系统功能权限
	 * @param sfId
	 * @param roleIds
	 */
	public void setSysFunctionRole(int sfId,List<Integer> roleIds){
		this.delBySfId(sfId); // 先删除之前的
		
		if(CollectionUtils.isEmpty(roleIds)) {
			return;
		}
		
		FunctionRoleParam param = new FunctionRoleParam();
		param.setRoleIds(roleIds);
		param.setSfId(sfId);
		
		this.getDao().setFunctionRole(param);
	}
	
	/**
	 * 设置系统功能权限
	 * @param roleId
	 * @param sfId
	 */
	public void setSysFunctionRole(List<Integer> sfIds,int roleId){
		delByRoleId(roleId); // 删除该角色所有的操作权限
		
		// 添加新的操作权限
		if(CollectionUtils.isNotEmpty(sfIds)){
			RRolePermission rp = null;
			for (Integer sfId : sfIds) {
				rp = new RRolePermission();
				rp.setRoleId(roleId);
				rp.setSfId(sfId);
				
				this.save(rp);
			}
		}
	}
	
	public void delBySfId(int sfId){
		this.getDao().delBySfId(sfId);
	}
	
	@Override
	public void delByRoleId(int roleId){
		this.getDao().delByRoleId(roleId);
	}
	
}
