package permission.controller;

import org.durcframework.core.MessageResult;
import org.durcframework.core.controller.SearchController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import permission.entity.RRole;
import permission.entity.RUserRole;
import permission.entity.SetUserRoleParam;
import permission.service.RRoleService;
import permission.service.RUserRoleService;

@Controller
public class RUserRoleController extends
		SearchController<RUserRole, RUserRoleService> {
	
	@Autowired
	private RRoleService roleService;

	@RequestMapping("/addUserRole.do")
	public @ResponseBody MessageResult addUserRole(SetUserRoleParam userRole) {
		if(userRole.getUserId() == 0 || userRole.getRoleId() == 0) {
			return this.error("添加角色失败");
		}
		
		RUserRole record = this.getService().get(userRole);
		if(record != null) {
			RRole role = roleService.get(userRole.getRoleId());
			return this.error("无法重复设置角色:" + role.getRoleName());
		}
		
		this.getService().setUserRole(userRole);
		
		return this.success();
	} 
	
	@RequestMapping("/delUserRole.do")
	public @ResponseBody MessageResult delUserRole(RUserRole setUserRole) {
		
		if(setUserRole.getUserId() == 0 || setUserRole.getRoleId() == 0) {
			return this.error("删除失败");
		}
		
		this.getService().del(setUserRole);
		
		return this.success();
	} 
}
