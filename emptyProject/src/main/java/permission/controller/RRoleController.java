package permission.controller;

import java.util.List;
import java.util.Map;

import org.durcframework.core.DefaultGridResult;
import org.durcframework.core.EntityProcessor;
import org.durcframework.core.GridResult;
import org.durcframework.core.MessageResult;
import org.durcframework.core.controller.CrudController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import permission.common.RMSContext;
import permission.entity.RRole;
import permission.entity.RRoleSch;
import permission.entity.RUserRole;
import permission.entity.SetRoleParam;
import permission.service.RRoleService;
import permission.service.RUserRoleService;

@Controller
public class RRoleController extends CrudController<RRole, RRoleService> {

	@Autowired
	private RUserRoleService userRoleService;
	
	@RequestMapping("/addRRole.do")
	public @ResponseBody MessageResult addRRole(SetRoleParam param) {
		if (StringUtils.hasText(param.getRoleName())) {
			RRole role = new RRole();
			role.setRoleName(param.getRoleName());
			this.getService().addRole(role, param.getSfId());
			return this.success();
		}
		return this.error("添加失败");
	}

	@RequestMapping("/listRRole.do")
	public @ResponseBody GridResult listRRole(RRoleSch searchEntity) {
		final int adminRoleId = RMSContext.getInstance().getAdminRoleId();
		return this.queryWithProcessor(searchEntity, new EntityProcessor<RRole>() {
			@Override
			public void process(RRole arg0, Map<String, Object> arg1) {
				if(adminRoleId == arg0.getRoleId()) {
					arg1.put("isAdmin", true);
				}
			}
		});
	}

	@RequestMapping("/updateRRole.do")
	public @ResponseBody MessageResult updateRRole(SetRoleParam param) {
		if (StringUtils.hasText(param.getRoleName())) {
			RRole role = new RRole();
			role.setRoleId(param.getRoleId());
			role.setRoleName(param.getRoleName());
			this.getService().updateRole(role, param.getSfId());
			return this.success();
		}
		return this.error("修改失败");
	}

	@RequestMapping("/delRRole.do")
	public @ResponseBody MessageResult delRRole(RRole entity) {
		int adminRoleId = RMSContext.getInstance().getAdminRoleId();
		if(adminRoleId == entity.getRoleId()) {
			return this.error("超级管理员无法删除!");
		}
		return this.delete(entity);
	}

	@RequestMapping("/listRoleRelationInfo.do")
	public @ResponseBody DefaultGridResult listRoleRelationInfo(int roleId) {

		List<RUserRole> userRoles = userRoleService.getUserRoleByRoleId(roleId);

		return new DefaultGridResult(userRoles);
	}
	
	@RequestMapping("/listAllRRole.do")
	public @ResponseBody GridResult listAllRRole(RRoleSch searchEntity) {
		return this.queryAll(searchEntity);
	}

}