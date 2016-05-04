package com.chinal.emp.controller;

import java.text.DecimalFormat;
import java.util.List;

import org.durcframework.core.controller.BaseController;
import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.subexpression.SqlExpression;
import org.durcframework.core.expression.subexpression.ValueExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.chinal.emp.entity.Employee;
import com.chinal.emp.security.AuthUser;
import com.chinal.emp.service.CustomerBasicService;
import com.chinal.emp.service.EmployeeService;
import com.chinal.emp.service.InsuranceRecordService;
import com.chinal.emp.service.SitRecordService;

@Controller
public class MainStatisticsController extends BaseController {

	@Autowired
	private CustomerBasicService customerBasicService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private SitRecordService visitRecordService;

	@Autowired
	private InsuranceRecordService insuranceRecordService;

	@RequestMapping("/openMainStatistics.do")
	public ModelAndView openEmployee() {

		SecurityContextImpl securityContextImpl = (SecurityContextImpl) getRequest().getSession()
				.getAttribute("SPRING_SECURITY_CONTEXT");

		AuthUser onlineUser = (AuthUser) securityContextImpl.getAuthentication().getPrincipal();

		ExpressionQuery empquery = new ExpressionQuery();
		ExpressionQuery cusquery = new ExpressionQuery();
		// 不同的级别，查询的用户数量不一样

		// 四级，五级查询全部
		if (onlineUser.getLevel() == 5 || onlineUser.getLevel() == 4) {

		}

		// 二级，三级查询自己及下属的
		if (onlineUser.getLevel() == 2 || onlineUser.getLevel() == 3) {
			String sql = "FIND_IN_SET(code, getChildList('" + onlineUser.getEmployee().getCode() + "'))";

			empquery.addSqlExpression(new SqlExpression(sql));
			List<Employee> emps = employeeService.findTree(empquery);
			if (emps.size() > 0) {
				StringBuffer empAccounts = new StringBuffer();
				for (Employee t_employee : emps) {
					empAccounts.append(",").append(t_employee.getAccount());
				}
				String cussql = "FIND_IN_SET(t.account, getChildList('" + empAccounts.toString().substring(1) + "'))";
				cusquery.addSqlExpression(new SqlExpression(cussql));
			}
		}

		// 一级查询自己负责的
		if (onlineUser.getLevel() == 1) {
			cusquery.add(new ValueExpression("t.account", onlineUser.getAccount()));
		}

		// 返回查询结果
		int count = customerBasicService.findTotalCount(cusquery);
		String visitPercent = "0.00";
		String insurancePercent = "0.00";
		Integer birthCount = 0;
		if (count > 0) {
			// 获得最近六个月的访问客户数
			ExpressionQuery visitquery = new ExpressionQuery();
			visitquery.addSqlExpression(
					new SqlExpression("t.visitTime  >= DATE_SUB(NOW(), INTERVAL 6 MONTH) and t.account='"
							+ onlineUser.getAccount() + "'"));
			Integer visitCount = visitRecordService.findVisitCount(visitquery);
			DecimalFormat format = new DecimalFormat("0.00");
			visitPercent = format.format(Double.parseDouble(visitCount + "") / Double.parseDouble(count + ""));

			// 获得所有开单的客户数
			ExpressionQuery insurancequery = new ExpressionQuery();
			insurancequery
					.addValueExpression(new ValueExpression("t.yewuyuan_code", onlineUser.getEmployee().getCode()));
			Integer insuranceCount = insuranceRecordService.findInsuranceCount(insurancequery);
			insurancePercent = format.format(insuranceCount / count);

			// 获得最近过生日的用户数
			cusquery = new ExpressionQuery();
			cusquery.addSqlExpression(new SqlExpression(
					"DAYOFYEAR(t.birthday)  >= DAYOFYEAR(NOW())  and DAYOFYEAR(t.birthday)  <= (DAYOFYEAR(NOW())+7)"));
			cusquery.addSqlExpression(new SqlExpression("t.account='" + onlineUser.getAccount() + "'"));
			birthCount = customerBasicService.findTotalCount(cusquery);

			// TODO 制式服务未录
		}
		ModelAndView mv = new ModelAndView();
		mv.addObject("cusNum", count);
		mv.addObject("visitPercent", Double.parseDouble(visitPercent) * 100);
		mv.addObject("birthCount", birthCount);
		mv.addObject("insurancePercent", Double.parseDouble(insurancePercent) * 100);
		mv.setViewName("mainstatistics");
		return mv;
	}

}
