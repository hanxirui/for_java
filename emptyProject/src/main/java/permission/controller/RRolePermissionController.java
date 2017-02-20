package permission.controller;

import java.util.List;

import org.durcframework.core.MessageResult;
import org.durcframework.core.controller.CrudController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import permission.entity.RRolePermission;
import permission.service.RRolePermissionService;

@Controller
public class RRolePermissionController extends
		CrudController<RRolePermission, RRolePermissionService> {

	@RequestMapping("listRolePermissionSfId_backuser.do")
	public @ResponseBody List<RRolePermission> listRolePermissionSfId(int sfId) {
		List<RRolePermission> list = this.getService().getRolePermissionBySfId(sfId);
		return list;
	}
	
	@RequestMapping("/listRolePermissionByRoleId.do")
	public @ResponseBody List<RRolePermission> listRolePermissionByRoleId(int roleId) {
		List<RRolePermission> list = this.getService().getRolePermissionByRole(roleId);
		return list;
	}
	
	@RequestMapping("/delRolePermission.do")
	public @ResponseBody MessageResult delRolePermission(RRolePermission rolePermission) {
		if(rolePermission.getRoleId() > 0 && rolePermission.getSfId() > 0) {
			this.delete(rolePermission);
			return this.success();
		}else{
			return this.error("设置权限失败");
		}
	}
	
	@RequestMapping("/addRolePermission.do")
	public @ResponseBody MessageResult addRolePermission(RRolePermission rolePermission) {
		return this.save(rolePermission);
	}
}
