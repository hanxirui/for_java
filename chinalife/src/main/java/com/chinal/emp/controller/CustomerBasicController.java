package com.chinal.emp.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.subexpression.LikeRightExpression;
import org.durcframework.core.expression.subexpression.SqlExpression;
import org.durcframework.core.expression.subexpression.ValueExpression;
import org.durcframework.core.support.BsgridController;
import org.durcframework.core.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.SAXException;

import com.alibaba.fastjson.JSONObject;
import com.chinal.emp.entity.CustomerBasic;
import com.chinal.emp.entity.CustomerBasicSch;
import com.chinal.emp.entity.Employee;
import com.chinal.emp.security.AuthUser;
import com.chinal.emp.service.CustomerBasicService;
import com.chinal.emp.service.EmployeeService;
import com.chinal.emp.util.FileUtils;

import net.sf.jxls.reader.ReaderBuilder;
import net.sf.jxls.reader.XLSReadStatus;
import net.sf.jxls.reader.XLSReader;

@Controller
public class CustomerBasicController extends BsgridController<CustomerBasic, CustomerBasicService> {

	private final static String xmlConfig = "userTrack.xml";

	protected static Logger logger = Logger.getLogger("controller");

	@Autowired
	HttpServletRequest request;

	@Autowired
	private EmployeeService employeeService;

	@RequestMapping("/openCustomerBasic.do")
	public String openCustomerBasic() {
		logger.debug("Received request to show common page");

		SecurityContextImpl securityContextImpl = (SecurityContextImpl) request.getSession()
				.getAttribute("SPRING_SECURITY_CONTEXT");
		AuthUser onlineUser = (AuthUser) securityContextImpl.getAuthentication().getPrincipal();
		// 登录名
		// System.out.println("Username:" +
		// securityContextImpl.getAuthentication().getName());
		// 登录密码，未加密的
		// System.out.println("Credentials:" +
		// securityContextImpl.getAuthentication().getCredentials());
		// WebAuthenticationDetails details = (WebAuthenticationDetails)
		// securityContextImpl.getAuthentication()
		// .getDetails();
		// 获得访问地址
		// System.out.println("RemoteAddress:" + details.getRemoteAddress());
		// 获得sessionid
		// System.out.println("SessionId:" + details.getSessionId());
		// 获得当前用户所拥有的权限
		// List<GrantedAuthority> authorities = (List<GrantedAuthority>)
		// securityContextImpl.getAuthentication()
		// .getAuthorities();
		// for (GrantedAuthority grantedAuthority : authorities) {
		// System.out.println("Authority:" + grantedAuthority.getAuthority());
		// }
		return "customerBasic";
	}

	@RequestMapping("/openCustomerForService.do")
	public String openCustomerForService() {
		return "customerForService";
	}

	@RequestMapping("/listCustomerForService.do")
	public ModelAndView listCustomerForService(CustomerBasicSch searchEntity) {
		ExpressionQuery query = new ExpressionQuery();
		SecurityContextImpl securityContextImpl = (SecurityContextImpl) getRequest().getSession()
				.getAttribute("SPRING_SECURITY_CONTEXT");
		AuthUser onlineUser = (AuthUser) securityContextImpl.getAuthentication().getPrincipal();
		query.addSqlExpression(new SqlExpression(
				"DAYOFYEAR(t.birthday)  >= DAYOFYEAR(NOW())  and DAYOFYEAR(t.birthday)  <= (DAYOFYEAR(NOW())+7)"));
		query.addSqlExpression(new SqlExpression("t.kehujingli='" + onlineUser.getEmployee().getCode() + "'"));
		// 返回查询结果
		return this.list(query);
	}

	@RequestMapping("/addCustomerBasic.do")
	public ModelAndView addCustomerBasic(CustomerBasic entity) {
		return this.add(entity);
	}

	@RequestMapping("/listCustomerBasic.do")
	public ModelAndView listCustomerBasic(CustomerBasicSch searchEntity) {
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
				StringBuffer empCodes = new StringBuffer();
				for (Employee t_employee : emps) {
					empCodes.append(",").append(t_employee.getCode());
				}
				String cussql = "FIND_IN_SET(t.kehujingli, getChildList('" + empCodes.toString().substring(1) + "'))";
				cusquery.addSqlExpression(new SqlExpression(cussql));
			}
		}

		// 一级查询自己负责的
		if (onlineUser.getLevel() == 1) {
			cusquery.add(new ValueExpression("t.kehujingli", onlineUser.getEmployee().getCode()));
		}
		// cusquery.addJoinExpression(new LeftJoinExpression("customer_extras",
		// "t2", "idcardnum", "idcardnum"));
		if (searchEntity.getName() != null) {
			cusquery.add(new LikeRightExpression("t.name", searchEntity.getName()));
		}

		// 返回查询结果
		return this.list(cusquery);
	}

	@RequestMapping("/listCustomerForEmp.do")
	public ModelAndView listCustomerForEmp() {

		AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		ExpressionQuery query = new ExpressionQuery();

		query.add(new ValueExpression("t.kehujingli", authUser.getEmployee().getCode()));

		// 返回查询结果
		return this.list(query);
	}

	@RequestMapping("/updateCustomerBasic.do")
	public ModelAndView updateCustomerBasic(CustomerBasic entity) {
		return this.modify(entity);
	}

	@RequestMapping("/fenpeiCustomer.do")
	public void fenpeiCustomer(String cusIds, String empId) {
		this.getService().fenpeiCustomer(cusIds, empId);
	}

	@RequestMapping("/delCustomerBasic.do")
	public ModelAndView delCustomerBasic(CustomerBasic entity) {
		return this.remove(entity);
	}

	@RequestMapping("/importCustomer.do")
	public void importCustomer(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			MultipartHttpServletRequest mulRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = mulRequest.getFile("filename");

			String date = DateUtil.format(new Date(), "yyyyMM");
			File dateFile = new File(request.getRealPath("/") + File.separator + date);
			if (!dateFile.exists()) {
				dateFile.mkdir();
			}
			File bakFile = new File(dateFile, file.getOriginalFilename());

			FileUtils.inputstream2file(file.getInputStream(), bakFile);

			// List<CustomerBasic> list = new ArrayList<CustomerBasic>();
			//
			// list = read(file);
			// if (CollectionUtils.isNotEmpty(list)) {
			// for (CustomerBasic user : list) {
			// this.add(user);
			// }
			// }
			result.put("status", "success");
		} catch (Exception e) {
			result.put("status", "error");
			e.printStackTrace();
		}
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(JSONObject.toJSON(result).toString());
	}

	public List<CustomerBasic> read(final MultipartFile file) {
		InputStream inputXML = new BufferedInputStream(this.getClass().getClassLoader().getResourceAsStream(xmlConfig));
		XLSReader mainReader;
		try {
			mainReader = ReaderBuilder.buildFromXML(inputXML);
			InputStream inputXLS = new BufferedInputStream(file.getInputStream());
			CustomerBasic stu = new CustomerBasic();
			List users = new ArrayList();
			Map beans = new HashMap();
			beans.put("user", stu);
			beans.put("users", users);
			XLSReadStatus readStatus = mainReader.read(inputXLS, beans);
			return users;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
