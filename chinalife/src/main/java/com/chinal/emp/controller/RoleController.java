package com.chinal.emp.controller;

import org.durcframework.core.support.BsgridController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.chinal.emp.entity.Role;
import com.chinal.emp.entity.RoleSch;
import com.chinal.emp.service.RoleService;

@Controller
public class RoleController extends BsgridController<Role, RoleService> {

	@RequestMapping("/openRole.do")
	public String openRole() {
		return "role";
	}

	@RequestMapping("/addRole.do")
	public ModelAndView addRole(Role entity) {
		return this.add(entity);
	}

	@RequestMapping("/listAllRole.do")
	public ModelAndView listAllRole(RoleSch searchEntity) {
		return this.listAll(searchEntity);
	}

	@RequestMapping("/listRole.do")
	public ModelAndView listRole(RoleSch searchEntity) {
		return this.list(searchEntity);
	}

	@RequestMapping("/updateRole.do")
	public ModelAndView updateRole(Role entity) {
		return this.modify(entity);
	}

	@RequestMapping("/delRole.do")
	public ModelAndView delRole(Role entity) {
		return this.remove(entity);
	}

}
