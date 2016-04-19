package com.chinal.emp.controller;

import org.apache.log4j.Logger;
import org.durcframework.core.support.BsgridController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.chinal.emp.entity.Employee;
import com.chinal.emp.entity.EmployeeSch;
import com.chinal.emp.service.EmployeeService;

@Controller
public class EmployeeController extends BsgridController<Employee, EmployeeService> {

	protected static Logger logger = Logger.getLogger("controller");

	/**
	 * 跳转到employee页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/employee.do", method = RequestMethod.GET)
	public String getAadminPage() {
		logger.debug("Received request to show admin page");
		return "employee";

	}

	@RequestMapping("/addEmployee.do")
	public ModelAndView addEmployee(Employee entity) {
		return this.add(entity);
	}

	@RequestMapping("/listEmployee.do")
	public ModelAndView listEmployee(EmployeeSch searchEntity) {
		return this.list(searchEntity);
	}

	@RequestMapping("/updateEmployee.do")
	public ModelAndView updateEmployee(Employee entity) {
		return this.modify(entity);
	}

	@RequestMapping("/delEmployee.do")
	public ModelAndView delEmployee(Employee entity) {
		return this.remove(entity);
	}

}
