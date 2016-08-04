package com.chinal.emp.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.durcframework.core.SearchEntity;
import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.subexpression.LikeRightExpression;
import org.durcframework.core.expression.subexpression.SqlExpression;
import org.durcframework.core.expression.subexpression.ValueExpression;
import org.durcframework.core.support.BsgridController;
import org.jxls.reader.ReaderBuilder;
import org.jxls.reader.XLSReadStatus;
import org.jxls.reader.XLSReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.SAXException;

import com.alibaba.fastjson.JSONObject;
import com.chinal.emp.entity.Bizplatform;
import com.chinal.emp.entity.CustomerBasic;
import com.chinal.emp.entity.CustomerBasicSch;
import com.chinal.emp.entity.Employee;
import com.chinal.emp.entity.Fenpeilishi;
import com.chinal.emp.entity.KpiInfo;
import com.chinal.emp.entity.Org;
import com.chinal.emp.security.AuthUser;
import com.chinal.emp.service.BizplatformService;
import com.chinal.emp.service.CustomerBasicService;
import com.chinal.emp.service.EmployeeService;
import com.chinal.emp.service.FenpeilishiService;
import com.chinal.emp.service.OrgService;
import com.chinal.emp.service.SitRecordService;
import com.chinal.emp.util.DateUtil;
import com.chinal.emp.util.FileUtils;

@Controller
public class CustomerBasicController extends BsgridController<CustomerBasic, CustomerBasicService> {

	private final static String xmlConfig = "userTrack.xml";

	protected static Logger logger = Logger.getLogger("controller");

	@Autowired
	HttpServletRequest request;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private OrgService orgService;

	@Autowired
	private BizplatformService bizplatformService;

	@Autowired
	private SitRecordService sitRecordService;

	@Autowired
	private FenpeilishiService fenpeilishiService;

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
	public ModelAndView openCustomerForService(String from) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("from", from);
		mv.setViewName("customerForService");
		return mv;
	}

	@RequestMapping("/openCustomerForVisit.do")
	public ModelAndView openCustomerForVisit(String from) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("from", from);
		mv.setViewName("customerForVisit");
		return mv;
	}

	@Deprecated
	@RequestMapping("/listCustomerForService.do")
	public ModelAndView listCustomerForService(CustomerBasicSch searchEntity, String from) {
		ExpressionQuery query = genCustomerQuery(searchEntity);

		// 生日提醒客户
		if (null != from && "b".equals(from)) {
			query.addSqlExpression(new SqlExpression(
					"DAYOFYEAR(t.birthday)  >= DAYOFYEAR(NOW())  and DAYOFYEAR(t.birthday)  <= (DAYOFYEAR(NOW())+7)"));
		}

		// 制式服务未录客户
		else if (null != from && "s".equals(from)) {
			ExpressionQuery bizQuery = new ExpressionQuery();
			bizQuery.addValueExpression(new ValueExpression("t.orgcode", getOnlineUser().getEmployee().getOrgcode()));
			bizQuery.addSqlExpression(new SqlExpression("t.startdate<'" + DateUtil.getShortFormatNow()
					+ "' and t.enddate>'" + DateUtil.getShortFormatNow() + "'"));
			List<Bizplatform> plats = bizplatformService.find(bizQuery);
			// 找到登录人员所有的客户
			if (plats != null && plats.size() > 0) {
				query.addSqlExpression(new SqlExpression(
						"t.idcardnum not in (select s.idcardnum from service_record s where s.content='"
								+ plats.get(0).getTitle() + "')"));
			}

		}

		// 返回查询结果
		return this.list(query);
	}

	@RequestMapping("/listCustomerForVisit.do")
	public ModelAndView listCustomerForVisit(CustomerBasicSch searchEntity, String from) {
		ExpressionQuery query = genCustomerQuery(searchEntity);

		// 生日提醒客户
		if (null != from && "b".equals(from)) {
			query.addSqlExpression(new SqlExpression(
					"DAYOFYEAR(t.birthday)  >= DAYOFYEAR(NOW())  and DAYOFYEAR(t.birthday)  <= (DAYOFYEAR(NOW())+7)"));
		}

		// 制式服务未录客户
		else if (null != from && "s".equals(from)) {
			ExpressionQuery bizQuery = new ExpressionQuery();
			bizQuery.addValueExpression(new ValueExpression("t.orgcode", getOnlineUser().getEmployee().getOrgcode()));
			bizQuery.addSqlExpression(new SqlExpression("t.startdate<'" + DateUtil.getShortFormatNow()
					+ "' and t.enddate>'" + DateUtil.getShortFormatNow() + "'"));
			List<Bizplatform> plats = bizplatformService.find(bizQuery);
			// 找到登录人员所有的客户
			if (plats != null && plats.size() > 0) {
				query.addSqlExpression(new SqlExpression(
						"t.idcardnum not in (select s.idcardnum from service_record s where s.content='"
								+ plats.get(0).getTitle() + "')"));
			}

		}
		// 返回查询结果
		ModelAndView mv = this.list(query);
		return mv;
	}

	@RequestMapping("/addCustomerBasic.do")
	public ModelAndView addCustomerBasic(CustomerBasic entity) {
		setOrg(entity);
		return this.add(entity);
	}

	@RequestMapping("/listCustomerBasic.do")
	public ModelAndView listCustomerBasic(CustomerBasicSch searchEntity) {
		ExpressionQuery cusquery = genCustomerQuery(searchEntity);
		// 返回查询结果
		ModelAndView mv = this.list(cusquery);
		return mv;
	}

	private ExpressionQuery genCustomerQuery(CustomerBasicSch searchEntity) {

		AuthUser onlineUser = getOnlineUser();

		ExpressionQuery empquery = new ExpressionQuery();
		ExpressionQuery cusquery = new ExpressionQuery();
		// 不同的级别，查询的用户数量不一样

		// 四级，五级查询全部
		if (onlineUser.getLevel() == 5 || onlineUser.getLevel() == 4) {

		}

		// 二级，三级查询自己及下属的
		else if (onlineUser.getLevel() == 2 || onlineUser.getLevel() == 3) {
			String sql = "FIND_IN_SET(code, getChildList('" + onlineUser.getEmployee().getCode() + "'))";

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
		else if (onlineUser.getLevel() == 1) {
			cusquery.add(new ValueExpression("t.empcode", onlineUser.getEmployee().getCode()));
		}
		if (searchEntity.getEmpname() != null && !"".equals(searchEntity.getEmpname())) {
			cusquery.add(new ValueExpression("t.empname", searchEntity.getEmpname()));
		}

		if (searchEntity.getEmporgcode() != null && !"".equals(searchEntity.getEmporgcode())
				&& !"0".equals(searchEntity.getEmporgcode())) {
			cusquery.add(new ValueExpression("t.emporgcode", searchEntity.getEmporgcode()));
		}

		if (searchEntity.getName() != null && !"".equals(searchEntity.getName())) {
			cusquery.add(new LikeRightExpression("t.name", searchEntity.getName()));
		}

		if (searchEntity.getVcount() != null && "NAN".equals(searchEntity.getVcount())) {
			cusquery.addSqlExpression(new SqlExpression("t.empcode is null or t.empcode=''"));
		} else if (searchEntity.getVcount() != null && !"".equals(searchEntity.getVcount())
				&& Integer.parseInt(searchEntity.getVcount()) == 1) {
			cusquery.addSqlExpression(new SqlExpression(
					"t.idcardnum in (SELECT  t4.idcardnum from (select count(idcardnum) vcount,idcardnum from visit_record group by idcardnum) t4 where t4.vcount>=1)"));
		} else if (searchEntity.getVcount() != null && !"".equals(searchEntity.getVcount())
				&& Integer.parseInt(searchEntity.getVcount()) == 2) {
			cusquery.addSqlExpression(new SqlExpression(
					"t.idcardnum in (SELECT  t4.idcardnum from (select count(idcardnum) vcount,idcardnum from visit_record group by idcardnum) t4 where t4.vcount=2)"));
		} else if (searchEntity.getVcount() != null && !"".equals(searchEntity.getVcount())
				&& Integer.parseInt(searchEntity.getVcount()) == 3) {
			cusquery.addSqlExpression(new SqlExpression(
					"t.idcardnum in (SELECT  t4.idcardnum from (select count(idcardnum) vcount,idcardnum from visit_record group by idcardnum) t4 where t4.vcount>=3)"));
		}

		cusquery.setPageSize(searchEntity.getPageSize()).setPageIndex(searchEntity.getPageIndex());
		cusquery.addSort(searchEntity.getSortname(), searchEntity.getSortorder());
		return cusquery;
	}

	@RequestMapping("/getCustomerCountByVisit.do")
	public ModelAndView getCustomerCountByVisit(int count) {
		// SecurityContextImpl securityContextImpl = (SecurityContextImpl)
		// getRequest().getSession()
		// .getAttribute("SPRING_SECURITY_CONTEXT");
		//
		// AuthUser onlineUser = (AuthUser)
		// securityContextImpl.getAuthentication().getPrincipal();
		//
		// ç empquery = new ExpressionQuery();
		// ExpressionQuery cusquery = new ExpressionQuery();
		// // 不同的级别，查询的用户数量不一样
		//
		// // 四级，五级查询全部
		// if (onlineUser.getLevel() == 5 || onlineUser.getLevel() == 4) {
		//
		// }
		//
		// // 二级，三级查询自己及下属的
		// else if (onlineUser.getLevel() == 2 || onlineUser.getLevel() == 3) {
		// String sql = "FIND_IN_SET(code, getChildList('" +
		// onlineUser.getEmployee().getCode() + "'))";
		//
		// empquery.addSqlExpression(new SqlExpression(sql));
		// List<Employee> emps = employeeService.findTree(empquery);
		// if (emps.size() > 0) {
		// StringBuffer empCodes = new StringBuffer();
		// for (Employee t_employee : emps) {
		// empCodes.append(",").append(t_employee.getCode());
		// }
		// String cussql = "FIND_IN_SET(t.empcode, '" +
		// empCodes.toString().substring(1) + "')";
		// cusquery.addSqlExpression(new SqlExpression(cussql));
		// }
		// }
		//
		// // 一级查询自己负责的
		// else if (onlineUser.getLevel() == 1) {
		// cusquery.add(new ValueExpression("t.kehujingli",
		// onlineUser.getEmployee().getCode()));
		// }
		// if (count == 2) {
		// cusquery.add(new ValueExpression("t.vcount", count));
		// }
		//
		// else if (count == 3) {
		// cusquery.add(new ValueExpression("t.vcount", count));
		// }
		CustomerBasicSch sch = new CustomerBasicSch();
		sch.setVcount(count + "");
		ExpressionQuery cusquery = genCustomerQuery(sch);
		// 返回查询结果
		ModelAndView mv = new ModelAndView();
		mv.addObject(DEF_MODEL_NAME, this.getService().findVisitCount(cusquery));
		mv.setViewName("render");
		return mv;
	}

	@RequestMapping("/listCustomerForEmp.do")
	public ModelAndView listCustomerForEmp(SearchEntity searchEntity) {
		ExpressionQuery query = genCustomerQuery(new CustomerBasicSch());
		// 返回查询结果
		return this.list(query);
	}

	@RequestMapping("/updateCustomerBasic.do")
	public ModelAndView updateCustomerBasic(CustomerBasic entity) {
		setOrg(entity);
		return this.modify(entity);
	}

	private void setOrg(CustomerBasic entity) {
		// 获得机构信息
		if (entity.getEmpcode().equals(getOnlineUser().getCode())) {
			entity.setEmporgcode(getOnlineUser().getEmployee().getOrgcode());
			entity.setEmporgname(getOnlineUser().getEmployee().getOrgname());
		} else {
			ExpressionQuery query = new ExpressionQuery();
			query.addValueExpression(new ValueExpression("code", entity.getEmpcode()));
			List<Employee> emps = employeeService.findSimple(query);

			if (emps.size() > 0) {
				query = new ExpressionQuery();
				query.addValueExpression(new ValueExpression("code", emps.get(0).getOrgcode()));
				List<Org> orgs = orgService.find(query);
				if (orgs.size() > 0) {
					entity.setEmporgcode(orgs.get(0).getCode());
					entity.setEmporgname(orgs.get(0).getName());
				}
			}
		}
	}

	@RequestMapping("/fenpeiCustomer.do")
	public void fenpeiCustomer(String cusIds, String empcode) {

		ExpressionQuery query = new ExpressionQuery();
		query.addValueExpression(new ValueExpression("t.code", empcode));
		List<Employee> emps = employeeService.findSimple(query);
		if (null != emps && emps.size() > 0) {
			Employee emp = emps.get(0);
			CustomerBasic basic = new CustomerBasic();
			basic.setEmpcode(empcode);
			basic.setEmpname(emp.getName());
			basic.setEmporgcode(emp.getOrgcode());
			basic.setEmporgname(emp.getOrgname());
			basic.setIdcardnum(cusIds);
			this.getService().fenpeiCustomer(basic);
			List<String> cusList = Arrays.asList(cusIds.split(","));
			for (String cusid : cusList) {
				Fenpeilishi fenpeilish = new Fenpeilishi();
				String trueCusid = cusid.substring(1, cusid.length() - 1);
				fenpeilish.setCustomerId(trueCusid);
				fenpeilish.setFenpeirenCode(getOnlineUser().getCode());
				fenpeilish.setFenpeirenName(getOnlineUser().getcName());
				fenpeilish.setKehujingliCode(empcode);
				fenpeilish.setKehujingliName(emp.getName());
				fenpeilish.setFenpeishijian(DateUtil.getShortFormatNow());
				fenpeilishiService.save(fenpeilish);
			}

		}
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

	@RequestMapping("/getKpiInfo.do")
	public ModelAndView getKpiInfo(String startTime, String endTime) {
		KpiInfo kpiInfo = new KpiInfo();
		int days = 1;
		ExpressionQuery query = genCustomerQuery(new CustomerBasicSch());
		if (null == startTime || null == endTime || "".equals(startTime) || "".equals(endTime)) {
			String currMonth = DateUtil.getCurrYear() + "-" + DateUtil.getCurrMonth();
			query.addSqlExpression(new SqlExpression("t.visittime like '" + currMonth + "%'"));
			days = DateUtil.getDayNumOfMonth(Integer.parseInt(DateUtil.getCurrYear()),
					Integer.parseInt(DateUtil.getCurrMonth()));
		} else {
			query.addSqlExpression(
					new SqlExpression("t.visittime<'" + endTime + "' and t.visittime>'" + startTime + "'"));
			kpiInfo.setStartTime(startTime);
			kpiInfo.setEndTime(endTime);
			try {
				days = DateUtil.daysBetween(startTime, endTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		int visitCount = sitRecordService.findTotalCount(query);
		kpiInfo.setBaifangliang(visitCount + "");
		kpiInfo.setRijunbaifangliang(visitCount / days + "");
		return this.render(kpiInfo);
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

	private AuthUser getOnlineUser() {
		SecurityContextImpl securityContextImpl = (SecurityContextImpl) getRequest().getSession()
				.getAttribute("SPRING_SECURITY_CONTEXT");
		AuthUser onlineUser = (AuthUser) securityContextImpl.getAuthentication().getPrincipal();
		return onlineUser;
	}
}
