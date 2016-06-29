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

import com.chinal.emp.entity.Bizplatform;
import com.chinal.emp.entity.Employee;
import com.chinal.emp.security.AuthUser;
import com.chinal.emp.service.BizplatformService;
import com.chinal.emp.service.CustomerBasicService;
import com.chinal.emp.service.EmployeeService;
import com.chinal.emp.service.InsuranceRecordService;
import com.chinal.emp.service.SitRecordService;
import com.chinal.emp.util.DateUtil;

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

	@Autowired
	private BizplatformService bizplatformService;

	@RequestMapping("/openMainStatistics.do")
	public ModelAndView openEmployee() {

		SecurityContextImpl securityContextImpl = (SecurityContextImpl) getRequest().getSession()
				.getAttribute("SPRING_SECURITY_CONTEXT");

		AuthUser onlineUser = (AuthUser) securityContextImpl.getAuthentication().getPrincipal();

		ExpressionQuery cusquery = genCustomerQueryByPrincipal(onlineUser);

		// 返回总数
		int count = customerBasicService.findTotalCount(cusquery);
		String visitPercent = "0.00";
		String insurancePercent = "0.00";
		Integer birthCount = 0;
		Integer unService = 0;
		if (count > 0) {
			// 获得最近六个月的访问客户数
			cusquery.addSqlExpression(new SqlExpression("t.visitTime  >= DATE_SUB(NOW(), INTERVAL 6 MONTH)"));
			Integer visitCount = visitRecordService.findVisitCount(cusquery);
			DecimalFormat format = new DecimalFormat("0.00");
			visitPercent = format.format(Double.parseDouble(visitCount + "") * 100 / Double.parseDouble(count + ""));

			// 获得所有开单的客户数
			ExpressionQuery insurancequery = genInsuranceQueryByPrincipal(onlineUser);
			insurancequery.addValueExpression(new ValueExpression("t.kaituoxindan", "!=", "分配"));
			Integer insuranceCount = insuranceRecordService.findInsuranceCount(insurancequery);
			insurancePercent = format.format(insuranceCount / count);

			// 获得最近过生日的用户数
			cusquery = genCustomerQueryByPrincipal(onlineUser);
			cusquery.addSqlExpression(new SqlExpression(
					"DAYOFYEAR(t.birthday)  >= DAYOFYEAR(NOW())  and DAYOFYEAR(t.birthday)  <= (DAYOFYEAR(NOW())+7)"));
			birthCount = customerBasicService.findTotalCount(cusquery);

			// 制式服务未录
			// 找到当前的制式服务
			ExpressionQuery bizQuery = new ExpressionQuery();
			bizQuery.addValueExpression(new ValueExpression("t.orgcode", getOnlineUser().getEmployee().getOrgcode()));
			bizQuery.addSqlExpression(new SqlExpression("t.startdate<'" + DateUtil.getShortFormatNow()
					+ "' and t.enddate>'" + DateUtil.getShortFormatNow() + "'"));
			List<Bizplatform> plats = bizplatformService.find(bizQuery);
			// 找到登录人员所有的客户
			if (plats != null && plats.size() > 0) {
				cusquery = genCustomerQueryByPrincipal(onlineUser);
				cusquery.addSqlExpression(new SqlExpression(
						"t.idcardnum not in (select s.idcardnum from service_record s where s.content='"
								+ plats.get(0).getTitle() + "')"));
				unService = customerBasicService.findTotalCount(cusquery);
			}

			//
			// SELECT c.* from customer_basic c where c.idcardnum not in (select
			// idcardnum from service_record s where s.content='基因检测') and
			// c.empcode='81000000'

		}
		ModelAndView mv = new ModelAndView();
		mv.addObject("cusNum", count);
		mv.addObject("visitPercent", visitPercent);
		mv.addObject("birthCount", birthCount);
		mv.addObject("unService", unService);
		mv.addObject("insurancePercent", Double.parseDouble(insurancePercent) * 100);
		mv.setViewName("mainstatistics");
		return mv;
	}

	private ExpressionQuery genCustomerQueryByPrincipal(AuthUser onlineUser) {
		ExpressionQuery cusquery = new ExpressionQuery();
		// 不同的级别，查询的用户数量不一样

		// 四级，五级查询全部
		if (onlineUser.getLevel() == 5 || onlineUser.getLevel() == 4) {

		}

		// 二级，三级查询自己及下属的
		if (onlineUser.getLevel() == 2 || onlineUser.getLevel() == 3) {
			String sql = "FIND_IN_SET(code, getChildList('" + onlineUser.getEmployee().getCode() + "'))";
			ExpressionQuery empquery = new ExpressionQuery();
			empquery.addSqlExpression(new SqlExpression(sql));
			List<Employee> emps = employeeService.findTree(empquery);
			if (emps.size() > 0) {
				StringBuffer empCodes = new StringBuffer();
				for (Employee t_employee : emps) {
					empCodes.append(",").append(t_employee.getCode());
				}
				String cussql = "FIND_IN_SET(t.empcode, '" + empCodes.toString().substring(1) + "')";
				cusquery.addSqlExpression(new SqlExpression(cussql));
			}
		}

		// 一级查询自己负责的
		if (onlineUser.getLevel() == 1) {
			cusquery.add(new ValueExpression("t.empcode", onlineUser.getEmployee().getCode()));
		}
		return cusquery;
	}

	private ExpressionQuery genInsuranceQueryByPrincipal(AuthUser onlineUser) {
		ExpressionQuery cusquery = new ExpressionQuery();
		// 不同的级别，查询的用户数量不一样

		// 四级，五级查询全部
		if (onlineUser.getLevel() == 5 || onlineUser.getLevel() == 4) {

		}

		// 二级，三级查询自己及下属的
		if (onlineUser.getLevel() == 2 || onlineUser.getLevel() == 3) {
			String sql = "FIND_IN_SET(code, getChildList('" + onlineUser.getEmployee().getCode() + "'))";
			ExpressionQuery empquery = new ExpressionQuery();
			empquery.addSqlExpression(new SqlExpression(sql));
			List<Employee> emps = employeeService.findTree(empquery);
			if (emps.size() > 0) {
				StringBuffer empCodes = new StringBuffer();
				for (Employee t_employee : emps) {
					empCodes.append(",").append(t_employee.getCode());
				}
				String cussql = "FIND_IN_SET(t.xinfenpeirenyuangonghao, '" + empCodes.toString().substring(1) + "')";
				cusquery.addSqlExpression(new SqlExpression(cussql));
			}
		}

		// 一级查询自己负责的
		if (onlineUser.getLevel() == 1) {
			cusquery.add(new ValueExpression("t.xinfenpeirenyuangonghao", onlineUser.getEmployee().getCode()));
		}
		return cusquery;
	}

	private AuthUser getOnlineUser() {
		SecurityContextImpl securityContextImpl = (SecurityContextImpl) getRequest().getSession()
				.getAttribute("SPRING_SECURITY_CONTEXT");
		AuthUser onlineUser = (AuthUser) securityContextImpl.getAuthentication().getPrincipal();
		return onlineUser;
	}
}
