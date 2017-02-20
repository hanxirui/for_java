package permission.controller;

import java.util.List;
import java.util.Map;

import org.durcframework.core.EntityProcessor;
import org.durcframework.core.GridResult;
import org.durcframework.core.MessageResult;
import org.durcframework.core.UserContext;
import org.durcframework.core.controller.CrudController;
import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.subexpression.DefaultJoinExpression;
import org.durcframework.core.expression.subexpression.SqlExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import permission.entity.RRole;
import permission.entity.RUser;
import permission.entity.RUserSch;
import permission.service.RRoleService;
import permission.service.RUserService;
import permission.util.PasswordUtil;

@Controller
public class RUserController extends CrudController<RUser, RUserService> {

	@Autowired
	private RRoleService roleService;

	/**
	 * 获取待添加的用户组成员
	 * 
	 * @param groupId
	 * @return
	 */
	@RequestMapping("/listGroupNoAddUser.do")
	public @ResponseBody GridResult listGroupNoAddUser(
			RUserSch searchEntity,
			@RequestParam(value = "groupIdSch", required = true, defaultValue = "0") int groupIdSch) {
		ExpressionQuery query = this.buildExpressionQuery(searchEntity);

		query.addJoinExpression(new DefaultJoinExpression(
				"LEFT JOIN r_group_user gu ON t.user_id = gu.user_id"));
		query.add(new SqlExpression("gu.group_id <> " + groupIdSch
				+ " OR gu.group_id IS NULL"));

		return this.query(query);
	}

	@RequestMapping("/addRUser.do")
	public @ResponseBody MessageResult addRUser(RUser entity) {
		String password = entity.getPassword(); // md5加密后的
		password = PasswordUtil.createHash(password);
		entity.setPassword(password);
		return this.save(entity);
	}

	@RequestMapping("/listRUser.do")
	public @ResponseBody GridResult listRUser(RUserSch searchEntity) {
		return this.queryWithProcessor(searchEntity,
				new EntityProcessor<RUser>() {
					@Override
					public void process(RUser entity,
							Map<String, Object> jsonObject) {
						List<RRole> userRoles = roleService.getUserRole(entity.getUserId());
						jsonObject.put("roles", userRoles);
					}
				});
	}

	@RequestMapping("/optState.do")
	public @ResponseBody MessageResult optState(RUser user) {
		try{
			this.getService().optState(user.getUserId(), user.getState());
		}catch(Exception e) {
			return this.error(e.getMessage());
		}
		return this.success();
	} 

	@RequestMapping("/resetUserPassword.do")
	public @ResponseBody MessageResult resetUserPassword(RUser user) {
		String newPwsd = this.getService().resetUserPassword(user);
		return this.success(newPwsd);
	}

	@RequestMapping("/updateUserPassword_backuser.do")
	public @ResponseBody MessageResult updateUserPassword(String oldPswd, String newPswd,
			String newPswd2) {

		if (!newPswd.equals(newPswd2)) {
			return this.error("两次输入的新密码不一样");
		}
		RUser user = UserContext.getInstance().getUser();
		RUser storeUser = this.getService().get(user);

		boolean isPswdCorrect = PasswordUtil.validatePassword(oldPswd,
				storeUser.getPassword());

		if (!isPswdCorrect) {
			return this.error("原密码输入有误");
		}

		this.getService().updateUserPassword(storeUser, newPswd);

		return this.success();
	}

}