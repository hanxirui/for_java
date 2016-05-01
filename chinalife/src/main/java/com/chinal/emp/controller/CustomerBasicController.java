package com.chinal.emp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.subexpression.LikeRightExpression;
import org.durcframework.core.expression.subexpression.ValueExpression;
import org.durcframework.core.support.BsgridController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.chinal.emp.entity.CustomerBasic;
import com.chinal.emp.entity.CustomerBasicSch;
import com.chinal.emp.expression.LeftJoinExpression;
import com.chinal.emp.security.AuthUser;
import com.chinal.emp.service.CustomerBasicService;

@Controller
public class CustomerBasicController extends BsgridController<CustomerBasic, CustomerBasicService> {
	protected static Logger logger = Logger.getLogger("controller");

	@Autowired
	HttpServletRequest request;

	@RequestMapping("/openCustomerBasic.do")
	public String openCustomerBasic() {
		logger.debug("Received request to show common page");

		SecurityContextImpl securityContextImpl = (SecurityContextImpl) request.getSession()
				.getAttribute("SPRING_SECURITY_CONTEXT");
		// 登录名
		System.out.println("Username:" + securityContextImpl.getAuthentication().getName());
		// 登录密码，未加密的
		System.out.println("Credentials:" + securityContextImpl.getAuthentication().getCredentials());
		WebAuthenticationDetails details = (WebAuthenticationDetails) securityContextImpl.getAuthentication()
				.getDetails();
		// 获得访问地址
		System.out.println("RemoteAddress:" + details.getRemoteAddress());
		// 获得sessionid
		System.out.println("SessionId:" + details.getSessionId());
		// 获得当前用户所拥有的权限
		List<GrantedAuthority> authorities = (List<GrantedAuthority>) securityContextImpl.getAuthentication()
				.getAuthorities();
		for (GrantedAuthority grantedAuthority : authorities) {
			System.out.println("Authority:" + grantedAuthority.getAuthority());
		}
		return "customerBasic";
	}

	@RequestMapping("/addCustomerBasic.do")
	public ModelAndView addCustomerBasic(CustomerBasic entity) {
		return this.add(entity);
	}

	@RequestMapping("/listCustomerBasic.do")
	public ModelAndView listCustomerBasic(CustomerBasicSch searchEntity) {
		ExpressionQuery query = new ExpressionQuery();
		/*
		 * 关联DEPARTMENT表 department:第二张表的名字 t2:department表的别名 DEPARTMENT:主表的字段
		 * ID:第二张表的字段
		 * 
		 * 这样就会拼接成:inner join department t2 on t.DEPARTMENT = t2.ID
		 */
		query.addJoinExpression(new LeftJoinExpression("customer_extras", "t2", "idcardnum", "idcardnum"));
		// 查询外语系的学生
		// query.add(new ValueExpression("t2.department_name", "外语系"));
		if (searchEntity.getName() != null) {
			query.add(new LikeRightExpression("t.name", searchEntity.getName()));
		}
		// if (searchEntity.getFuzerenName() != null) {
		// query.add(new LikeRightExpression("t2.name",
		// searchEntity.getFuzerenName()));
		// }

		// 返回查询结果

		return this.list(query);
	}

	@RequestMapping("/listCustomerForEmp.do")
	public ModelAndView listCustomerForEmp() {

		AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		ExpressionQuery query = new ExpressionQuery();

		query.add(new ValueExpression("t.account", authUser.getAccount()));

		// 返回查询结果
		return this.list(query);
	}

	@RequestMapping("/updateCustomerBasic.do")
	public ModelAndView updateCustomerBasic(CustomerBasic entity) {
		return this.modify(entity);
	}

	@RequestMapping("/delCustomerBasic.do")
	public ModelAndView delCustomerBasic(CustomerBasic entity) {
		return this.remove(entity);
	}

}
