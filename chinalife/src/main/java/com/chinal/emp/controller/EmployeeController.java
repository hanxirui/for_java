package com.chinal.emp.controller;

import org.durcframework.core.support.BsgridController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.chinal.emp.entity.Employee;
import com.chinal.emp.entity.EmployeeSch;
import com.chinal.emp.service.EmployeeService;

@Controller
public class EmployeeController extends BsgridController<Employee, EmployeeService> {

	@Autowired
	Md5PasswordEncoder passwordEncoder;

	@RequestMapping("/openEmployee.do")
	public String openEmployee() {
		return "employee";
	}

	@RequestMapping("/addEmployee.do")
	public ModelAndView addEmployee(Employee entity) {
		if (entity.getPassword() != null && !"".equals(entity.getPassword())) {
			entity.setPassword(passwordEncoder.encodePassword(entity.getPassword(), entity.getAccount()));
		}
		return this.add(entity);
	}

	@RequestMapping("/listEmployee.do")
	public ModelAndView listEmployee(EmployeeSch searchEntity) {
		return this.list(searchEntity);
	}

	@RequestMapping("/updateEmployee.do")
	public ModelAndView updateEmployee(Employee entity) {
		if (entity.getPassword() != null && !"".equals(entity.getPassword())) {
			entity.setPassword(passwordEncoder.encodePassword(entity.getPassword(), entity.getAccount()));
		}
		return this.modify(entity);
	}

	@RequestMapping("/delEmployee.do")
	public ModelAndView delEmployee(Employee entity) {
		return this.remove(entity);
	}

}
