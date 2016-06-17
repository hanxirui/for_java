package com.chinal.emp.controller;

import java.util.List;

import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.subexpression.SqlExpression;
import org.durcframework.core.expression.subexpression.ValueExpression;
import org.durcframework.core.support.BsgridController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.chinal.emp.entity.ClaimRecord;
import com.chinal.emp.entity.ClaimRecordSch;
import com.chinal.emp.entity.Employee;
import com.chinal.emp.security.AuthUser;
import com.chinal.emp.service.ClaimRecordService;
import com.chinal.emp.service.EmployeeService;

@Controller
public class ClaimRecordController extends BsgridController<ClaimRecord, ClaimRecordService> {

	@Autowired
	private EmployeeService employeeService;

	@RequestMapping("/openClaimRecord.do")
	public String openClaimRecord() {
		return "claimRecord";
	}

	@RequestMapping("/addClaimRecord.do")
	public ModelAndView addClaimRecord(ClaimRecord entity) {
		if (null == entity.getFirstaccount() || "".equals(entity.getFirstaccount())) {
			entity.setFirstaccount(getOnlineUser().getCode());
		}
		return this.add(entity);
	}

	@RequestMapping("/listClaimRecord.do")
	public ModelAndView listClaimRecord(ClaimRecordSch searchEntity) {

		List<Employee> employees = employeeService.findSimple(genEmployeeQuery());
		StringBuffer empcode = new StringBuffer();
		for (Employee employee : employees) {
			empcode.append("," + employee.getCode());
		}
		ExpressionQuery query = this.buildExpressionQuery(searchEntity);
		if (null != empcode && empcode.length() > 1) {
			query.addSqlExpression(
					new SqlExpression("FIND_IN_SET(t.firstaccount , '" + empcode.toString().substring(1) + "')"));
		}
		return this.list(query);
	}

	@RequestMapping("/updateClaimRecord.do")
	public ModelAndView updateClaimRecord(ClaimRecord entity) {
		if (null != entity.getSecondaccount() && !"".equals(entity.getSecondaccount())
				&& getOnlineUser().getLevel() == 2) {
			entity.setSecondaccount(getOnlineUser().getCode());
		}
		if (null != entity.getThirdaccount() && !"".equals(entity.getThirdaccount())
				&& getOnlineUser().getLevel() == 3) {
			entity.setThirdaccount(getOnlineUser().getCode());
		}
		if (null != entity.getFourthaccount() && !"".equals(entity.getFourthaccount())
				&& getOnlineUser().getLevel() == 4) {
			entity.setFourthaccount(getOnlineUser().getCode());
		}
		return this.modify(entity);
	}

	@RequestMapping("/delClaimRecord.do")
	public ModelAndView delClaimRecord(ClaimRecord entity) {
		return this.remove(entity);
	}

	private AuthUser getOnlineUser() {
		SecurityContextImpl securityContextImpl = (SecurityContextImpl) getRequest().getSession()
				.getAttribute("SPRING_SECURITY_CONTEXT");

		AuthUser onlineUser = (AuthUser) securityContextImpl.getAuthentication().getPrincipal();

		return onlineUser;
	}

	private ExpressionQuery genEmployeeQuery() {
		AuthUser onlineUser = getOnlineUser();

		ExpressionQuery empquery = new ExpressionQuery();
		// 不同的级别，查询的用户数量不一样

		// 四级，五级查询全部
		if (onlineUser.getLevel() == 5 || onlineUser.getLevel() == 4) {

		}

		// 二级，三级查询自己及下属的
		else if (onlineUser.getLevel() == 2 || onlineUser.getLevel() == 3) {
			String sql = "FIND_IN_SET(code, getChildList('" + onlineUser.getEmployee().getCode() + "'))";

			ExpressionQuery tmpquery = new ExpressionQuery();
			tmpquery.addSqlExpression(new SqlExpression(sql));
			List<Employee> emps = employeeService.findTree(tmpquery);
			if (emps.size() > 0) {
				StringBuffer empCodes = new StringBuffer();
				for (Employee t_employee : emps) {
					empCodes.append(",").append(t_employee.getCode());
				}

				String cussql = "FIND_IN_SET(t.code, '" + empCodes.toString().substring(1) + "')";
				empquery.addSqlExpression(new SqlExpression(cussql));
			}
		}

		// 一级查询自己负责的
		else if (onlineUser.getLevel() == 1) {
			empquery.add(new ValueExpression("t.code", onlineUser.getEmployee().getCode()));
		}

		return empquery;
	}

}
