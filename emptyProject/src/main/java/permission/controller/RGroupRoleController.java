package permission.controller;

import org.apache.commons.lang.StringUtils;
import org.durcframework.core.GridResult;
import org.durcframework.core.MessageResult;
import org.durcframework.core.controller.CrudController;
import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.subexpression.ValueExpression;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import permission.entity.RGroupRole;
import permission.entity.RGroupRoleSch;
import permission.service.RGroupRoleService;

@Controller
public class RGroupRoleController extends
		CrudController<RGroupRole, RGroupRoleService> {

	@RequestMapping("/listRoleByGroupId.do")
	public @ResponseBody GridResult listRoleByGroupId(
			@RequestParam(value = "groupId", required = true, defaultValue = "0") int groupId) {
		ExpressionQuery query = ExpressionQuery.buildQueryAll();
		query.add(new ValueExpression("group_id", groupId));
		return this.query(query);
	}

	@RequestMapping("/addRGroupRole.do")
	public @ResponseBody MessageResult addRGroupRole(int groupId, String roleName) {
		if(StringUtils.isNotBlank(roleName)) {
			this.getService().addGroupRole(groupId, roleName);
			return this.success();
		}
		return this.error("添加失败");
	}

	@RequestMapping("/listRGroupRole.do")
	public @ResponseBody GridResult listRGroupRole(RGroupRoleSch searchEntity) {
		return this.query(searchEntity);
	}

	@RequestMapping("/delRGroupRole.do")
	public @ResponseBody MessageResult delRGroupRole(RGroupRole entity) {
		return this.delete(entity);
	}

}