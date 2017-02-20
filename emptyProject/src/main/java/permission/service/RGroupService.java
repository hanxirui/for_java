package permission.service;

import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.subexpression.ValueExpression;
import org.durcframework.core.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import permission.dao.RGroupDao;
import permission.entity.RGroup;

@Service
public class RGroupService extends CrudService<RGroup, RGroupDao> implements IGroup{
	
	@Autowired
	private RGroupUserService groupUserService;
	@Autowired
	private RGroupRoleService groupRoleService;
	
	/**
	 * 判断是否有子节点
	 * @param group
	 * @return
	 */
	public boolean hasChild(RGroup group){
		ExpressionQuery query = new ExpressionQuery();
		query.add(new ValueExpression("parent_id", group.getGroupId()));
		int count = this.findTotalCount(query);
		
		return count > 0;
	}
	
	/**
	 * 删除用户组
	 * 步骤:1.删除角色相关;2.删除用户组相关;
	 */
	@Override
	public int del(RGroup entity) {
		int groupId = entity.getGroupId();
		// 删除用户组角色
		groupRoleService.delByGroupId(groupId);
		// 移除用户组成员
		groupUserService.delByGroupId(groupId);
		// 删除自身
		this.delByGroupId(groupId);
		
		return 0;
	}

	@Override
	public void delByGroupId(int groupId) {
		RGroup group = new RGroup();
		group.setGroupId(groupId);
		this.getDao().del(group);
	}
	
	
}