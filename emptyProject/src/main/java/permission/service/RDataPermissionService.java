package permission.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.durcframework.core.expression.Expression;
import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.subexpression.InnerJoinExpression;
import org.durcframework.core.expression.subexpression.ListExpression;
import org.durcframework.core.expression.subexpression.ValueExpression;
import org.durcframework.core.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import permission.dao.RDataPermissionDao;
import permission.entity.RDataPermission;

@Service
public class RDataPermissionService extends
		CrudService<RDataPermission, RDataPermissionDao> {

	@Autowired
	private RDataPermissionRoleService dataPermissionRoleService;

	/**
	 * 保存数据权限
	 * @param dataPermission
	 */
	public void saveDataPermission(RDataPermission dataPermission) {
		this.save(dataPermission);

		dataPermissionRoleService.saveDataPermissionRole(
				dataPermission.getDpId(), dataPermission.getRoleId());
	}
	
	/**
	 * 修改数据权限
	 * @param dataPermission
	 */
	public void updateDataPermission(RDataPermission dataPermission) {
		// 删除原有关系
		dataPermissionRoleService.delByDpId(dataPermission.getDpId());
		// 新建关系
		dataPermissionRoleService.saveDataPermissionRole(
				dataPermission.getDpId(), dataPermission.getRoleId());
		
		this.update(dataPermission);
	}
	
	/**
	 * 删除数据权限
	 * @param dataPermission
	 */
	public void delDataPermission(RDataPermission dataPermission) {
		
		dataPermissionRoleService.delByDpId(dataPermission.getDpId());
		
		this.del(dataPermission);
	}

	/**
	 * 构建数据权限条件
	 * 
	 * @param roleIds
	 * @param sfId
	 * @return
	 */
	public List<Expression> buildDataExpresstions(List<Integer> roleIds,
			int sfId) {
		ExpressionQuery query = ExpressionQuery.buildQueryAll();

		query.add(new InnerJoinExpression("r_data_permission_role", "t2",
				"dp_id", "dp_id"));

		query.add(new ValueExpression("sr_id", sfId));
		query.add(new ListExpression("t2.role_id", roleIds));

		List<RDataPermission> list = this.find(query);

		if (CollectionUtils.isEmpty(list)) {
			return Collections.emptyList();
		}

		List<Expression> expressions = new ArrayList<Expression>(list.size());

		for (RDataPermission dataPermission : list) {
			expressions.add(ExpressionFactory.build(dataPermission));
		}

		return expressions;
	}
}
