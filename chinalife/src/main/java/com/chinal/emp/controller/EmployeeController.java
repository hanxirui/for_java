package com.chinal.emp.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.subexpression.LikeRightExpression;
import org.durcframework.core.support.BsgridController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.chinal.emp.entity.Employee;
import com.chinal.emp.entity.EmployeeSch;
import com.chinal.emp.entity.Role;
import com.chinal.emp.expression.LeftJoinExpression;
import com.chinal.emp.service.EmployeeService;
import com.chinal.emp.service.RoleService;

@Controller
public class EmployeeController extends BsgridController<Employee, EmployeeService> {

	@Autowired
	Md5PasswordEncoder passwordEncoder;

	@Autowired
	RoleService roleService;

	@RequestMapping("/openEmployee.do")
	public String openEmployee() {
		return "employee";
	}

	@RequestMapping("checkPassword.do")
	public void checkPassword(final HttpServletRequest request, final HttpServletResponse response) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String oldPwd = request.getParameter("oldpassword").trim();
		String id = request.getParameter("id").trim();
		String pwd = passwordEncoder.encodePassword(oldPwd, userDetails.getUsername());
		response.setContentType("text/html;charset=UTF-8");
		Employee user = this.get(Integer.parseInt(id));
		try {
			if (pwd.equals(user.getPassword())) {
				response.getWriter().print(true);
			} else {
				response.getWriter().print(false);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("updatePassword.do")
	public ModelAndView updatePassword(Employee entity) {
		if (entity.getPassword() != null && !"".equals(entity.getPassword())) {
			entity.setPassword(passwordEncoder.encodePassword(entity.getPassword(), entity.getAccount()));
		}
		return this.modify(entity);
	}

	@RequestMapping("/addEmployee.do")
	public ModelAndView addEmployee(Employee entity) {

		entity.setPassword(passwordEncoder.encodePassword("123456", entity.getAccount()));

		return this.add(entity);
	}

	@RequestMapping("/listEmployee.do")
	public ModelAndView listEmployee(EmployeeSch searchEntity) {

		ExpressionQuery query = new ExpressionQuery();

		query.addJoinExpression(new LeftJoinExpression("role", "t2", "role", "id"));
		query.addJoinExpression(new LeftJoinExpression("employee", "t3", "managercode", "code"));

		if (searchEntity.getName() != null) {
			query.add(new LikeRightExpression("t.name", searchEntity.getName()));
		}

		// 返回查询结果

		return this.list(query);

	}

	@RequestMapping("/getAllManagers.do")
	public ModelAndView getAllManagers(EmployeeSch searchEntity) {

		Role role = roleService.get(searchEntity.getRole());

		ExpressionQuery query = new ExpressionQuery();
		/*
		 * 关联DEPARTMENT表 department:第二张表的名字 t2:department表的别名 DEPARTMENT:主表的字段
		 * ID:第二张表的字段
		 * 
		 * 这样就会拼接成:inner join department t2 on t.DEPARTMENT = t2.ID
		 */
		query.addJoinExpression(new LeftJoinExpression("role", "t2", "role", "id"));
		query.addJoinExpression(new LeftJoinExpression("employee", "t3", "managercode", "code"));

		// query.addValueExpression(new ValueExpression("t2.level", ">=",
		// role.getLevel()));

		return this.listAll(query);
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

	public static void main(String[] args) {
		Md5PasswordEncoder md5 = new Md5PasswordEncoder();
		System.out.println(md5.encodePassword("123456", "zqs"));
	}
}
