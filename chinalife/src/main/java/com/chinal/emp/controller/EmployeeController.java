package com.chinal.emp.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.subexpression.InnerJoinExpression;
import org.durcframework.core.expression.subexpression.LikeRightExpression;
import org.durcframework.core.expression.subexpression.SqlExpression;
import org.durcframework.core.expression.subexpression.ValueExpression;
import org.durcframework.core.support.BsgridController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.chinal.emp.entity.Employee;
import com.chinal.emp.entity.EmployeeSch;
import com.chinal.emp.entity.Role;
import com.chinal.emp.expression.LeftJoinExpression;
import com.chinal.emp.security.AuthUser;
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
		AuthUser userDetails = getAuthUser();
		String oldPwd = request.getParameter("oldpassword").trim();
		String id = request.getParameter("id").trim();
		String pwd = passwordEncoder.encodePassword(oldPwd, userDetails.getEmployee().getCode());
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
			entity.setPassword(passwordEncoder.encodePassword(entity.getPassword(), entity.getCode()));
		}
		return this.modify(entity);
	}

	@RequestMapping("resetPassword.do")
	public ModelAndView resetPassword(Employee entity) {
		entity.setPassword(passwordEncoder.encodePassword("123456", entity.getCode()));
		return this.modify(entity);
	}

	@RequestMapping("/addEmployee.do")
	public ModelAndView addEmployee(Employee entity) {

		entity.setPassword(passwordEncoder.encodePassword("123456", entity.getCode()));

		return this.add(entity);
	}

	@RequestMapping("/listEmployee.do")
	public ModelAndView listEmployee(EmployeeSch searchEntity) {

		ExpressionQuery query = new ExpressionQuery();

		AuthUser onlineUser = getAuthUser();

		// 四级，五级查询自己部门的
		query.addValueExpression(new ValueExpression("t.orgcode", onlineUser.getEmployee().getOrgcode()));

		// 三级查询自己及下属的
		if (onlineUser.getLevel() == 3) {
			String sql = "FIND_IN_SET(t.code, getChildList('" + onlineUser.getEmployee().getCode() + "'))";
			query.addSqlExpression(new SqlExpression(sql));
		}
		// （admin可以看到全部）
		if (onlineUser.getCode().equals("admin")) {
			query = this.buildExpressionQuery(searchEntity);
		}

		if (searchEntity.getName() != null) {
			query.add(new LikeRightExpression("t.name", searchEntity.getName()));
		}
		if (searchEntity.getCode() != null) {
			query.add(new LikeRightExpression("t.code", searchEntity.getCode()));
		}
		if (searchEntity.getOrgname() != null) {
			query.add(new LikeRightExpression("t.orgname", searchEntity.getOrgname()));
		}

		query.addJoinExpression(new LeftJoinExpression("role", "t2", "role", "id"));
		query.addJoinExpression(new LeftJoinExpression("employee", "t3", "managercode", "code"));

		query.addPaginationInfo(searchEntity);
		// 返回查询结果
		return this.list(query);

	}

	@RequestMapping("/listEmployeeForCus.do")
	public ModelAndView listEmployeeForCus(EmployeeSch searchEntity, String empcode) {

		ExpressionQuery query = new ExpressionQuery();

		AuthUser onlineUser = getAuthUser();

		// 三级,四级，五级查询自己部门的
		if (onlineUser.getLevel() >= 3) {
			query.addValueExpression(new ValueExpression("t.orgcode", onlineUser.getEmployee().getOrgcode()));
			// String sql = "FIND_IN_SET(t.code, getChildList('" +
			// onlineUser.getEmployee().getCode() + "'))";
			// query.addSqlExpression(new SqlExpression(sql));
		} else {
			query.addValueExpression(new ValueExpression("t.code", onlineUser.getEmployee().getCode()));

		}

		// 编辑界面，回显的时候用
		if (empcode != null && !"".equals(empcode)) {
			query.addValueExpression(new ValueExpression("t.code", empcode));
		}

		query.addJoinExpression(new LeftJoinExpression("role", "t2", "role", "id"));
		query.addJoinExpression(new LeftJoinExpression("employee", "t3", "managercode", "code"));

		query.setPageSize(searchEntity.getPageSize());
		query.setPageIndex(searchEntity.getPageIndex());
		// 返回查询结果
		return this.list(query);

	}

	private AuthUser getAuthUser() {
		SecurityContextImpl securityContextImpl = (SecurityContextImpl) getRequest().getSession()
				.getAttribute("SPRING_SECURITY_CONTEXT");

		AuthUser onlineUser = (AuthUser) securityContextImpl.getAuthentication().getPrincipal();
		return onlineUser;
	}

	@RequestMapping("/getAllManagers.do")
	public ModelAndView getAllManagers(String roleId, String orgcode) {

		Role role = roleService.get(Integer.valueOf(roleId));

		ExpressionQuery query = new ExpressionQuery();

		query.addJoinExpression(new InnerJoinExpression("role", "t2", "role", "id"));
		query.addJoinExpression(new LeftJoinExpression("employee", "t3", "managercode", "code"));

		if (role.getLevel() < 4) {
			if (null == orgcode || "".equals(orgcode)) {
				AuthUser userDetails = getAuthUser();
				query.addValueExpression(new ValueExpression("t.orgcode", userDetails.getEmployee().getOrgcode()));
			} else {
				query.addValueExpression(new ValueExpression("t.orgcode", orgcode));
			}
		}
		query.addValueExpression(new ValueExpression("t2.level", ">=", role.getLevel()));

		return this.listAll(query);
	}

	@RequestMapping("/updateEmployee.do")
	public ModelAndView updateEmployee(Employee entity) {

		if (entity.getPassword() != null && !"".equals(entity.getPassword())) {
			entity.setPassword(passwordEncoder.encodePassword(entity.getPassword(), entity.getCode()));
		}
		return this.modify(entity);
	}

	@RequestMapping("/delEmployee.do")
	public ModelAndView delEmployee(Employee entity) {
		return this.remove(entity);
	}

	public static void main(String[] args) {
		Md5PasswordEncoder md5 = new Md5PasswordEncoder();
		System.out.println(md5.encodePassword("123456", "admin"));
	}
}
