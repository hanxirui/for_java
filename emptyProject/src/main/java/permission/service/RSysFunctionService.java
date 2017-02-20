package permission.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.subexpression.ValueExpression;
import org.durcframework.core.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import permission.common.UserOperation;
import permission.common.UserPermission;
import permission.dao.RSysFunctionDao;
import permission.entity.RSysFunction;

@Service
public class RSysFunctionService extends CrudService<RSysFunction, RSysFunctionDao> {
	
	@Autowired
	private RRolePermissionService rolePermissionService;
	
	/**
	 * 删除系统功能
	 */
	@Override
	public int del(RSysFunction sysFunction) {
		// 先删除角色
		rolePermissionService.delBySfId(sysFunction.getSfId());
		
		this.getDao().del(sysFunction);
		
		return 0;
	}
	
	/**
	 * 通过资源ID查找系统功能
	 * @param srId
	 * @return
	 */
	public List<RSysFunction> getBySySResId(int srId){
		ExpressionQuery query = ExpressionQuery.buildQueryAll();
    	query.add(new ValueExpression("sr_id", srId));
    	
    	return find(query);
	}
	
	/**
	 * 获取用户系统功能
	 * @param userId
	 * @return
	 */
	public List<RSysFunction> getUserSysFunction(int userId){
		return this.getDao().findUserSysFunction(userId);
	}
	
	/**
	 * 构建用户权限
	 * @param userId
	 * @return
	 */
	public UserPermission buildUserPermission(int userId) {
		List<RSysFunction> userSysFuns = this.getUserSysFunction(userId);
		
		UserPermission userPermission = new UserPermission();
		
		for (RSysFunction sysFun : userSysFuns) {
			userPermission.addPermission(String.valueOf(sysFun.getSrId())
					, new UserOperation(sysFun.getOperateCode(), sysFun.getUrl()));
		}
		
		return userPermission;
	}
	
	public List<RSysFunction> getByOperateCode(String operateCode){
		ExpressionQuery query = ExpressionQuery.buildQueryAll();
    	query.add(new ValueExpression("operate_code", operateCode));
    	
    	return find(query);
	}
	
	/**
	 * 根据资源ID删除系统功能
	 * @param srId
	 */
	public void delBySrId(int srId){
		List<RSysFunction> sysFuncs = getBySrId(srId);
		for (RSysFunction rSysFunction : sysFuncs) {
			del(rSysFunction);
		}
	}
	
	private List<RSysFunction> getBySrId(int srId){
		ExpressionQuery query = ExpressionQuery.buildQueryAll();
    	query.add(new ValueExpression("sr_id", srId));
    	
    	return find(query);
	}
	
	public RSysFunction getByOperateCodeSrId(String operateCode,int srId){
		ExpressionQuery query = new ExpressionQuery();
		query.setLimit(1);
    	query.add(new ValueExpression("operate_code", operateCode));
    	query.add(new ValueExpression("sr_id", srId));
    	
    	List<RSysFunction> list = find(query);
    	
    	if(CollectionUtils.isNotEmpty(list)){
    		return list.get(0);
    	}
    	
    	return null;
	}
	
	public boolean isExistSysFun(String operateCode,int srId){
		return this.getByOperateCodeSrId(operateCode, srId) != null;
	}
}
