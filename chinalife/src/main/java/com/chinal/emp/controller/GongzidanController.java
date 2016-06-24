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

import org.apache.commons.collections.CollectionUtils;
import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.subexpression.SqlExpression;
import org.durcframework.core.expression.subexpression.ValueExpression;
import org.durcframework.core.support.BsgridController;
import org.durcframework.core.util.DateUtil;
import org.jxls.reader.ReaderBuilder;
import org.jxls.reader.ReaderConfig;
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
import com.chinal.emp.entity.Employee;
import com.chinal.emp.entity.Gongzidan;
import com.chinal.emp.entity.GongzidanRender;
import com.chinal.emp.entity.GongzidanSch;
import com.chinal.emp.security.AuthUser;
import com.chinal.emp.service.CustomerBasicService;
import com.chinal.emp.service.EmployeeService;
import com.chinal.emp.service.GongzidanService;
import com.chinal.emp.util.FileUtils;

@Controller
public class GongzidanController extends BsgridController<Gongzidan, GongzidanService> {

	private final static String xmlConfig = "GongzidanRecord.xml";
	private static final String CONTENT_TYPE = "application/vnd.ms-excel;charset=GBK";
	private static final String F_HEADER_ARGU1 = "Content-Disposition";
	private static final String F_HEADER_ARGU2 = "attachment;filename=";

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private CustomerBasicService customerBasicService;

	@RequestMapping("/openGongzidan.do")
	public String openGongzidan() {
		return "gongzidan";
	}

	@RequestMapping("/addGongzidan.do")
	public ModelAndView addGongzidan(Gongzidan entity) {
		return this.add(entity);
	}

	@RequestMapping("/listGongzidan.do")
	public ModelAndView listGongzidan(GongzidanSch searchEntity) {
		ExpressionQuery t_query = genEmployeeQuery();
		t_query.setLimit(1000);
		List<Employee> employees = employeeService.findSimple(t_query);
		StringBuffer empcardnum = new StringBuffer();
		for (Employee employee : employees) {
			empcardnum.append("," + employee.getCode());
		}
		ExpressionQuery query = this.buildExpressionQuery(searchEntity);
		if (null != empcardnum && empcardnum.length() > 1) {
			query.addSqlExpression(
					new SqlExpression("FIND_IN_SET(t.gonghao , '" + empcardnum.toString().substring(1) + "')"));
		}
		return this.list(query);

	}

	@RequestMapping("/updateGongzidan.do")
	public ModelAndView updateGongzidan(Gongzidan entity) {
		return this.modify(entity);
	}

	@RequestMapping("/delGongzidan.do")
	public ModelAndView delGongzidan(Gongzidan entity) {
		return this.remove(entity);
	}

	@RequestMapping("/importGongzidan.do")
	public void importGongzidan(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			MultipartHttpServletRequest mulRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = mulRequest.getFile("filename");

			// 备份文件
			String date = DateUtil.format(new Date(), "yyyyMM");
			File dateFile = new File(request.getRealPath("/") + File.separator + date);
			if (!dateFile.exists()) {
				dateFile.mkdir();
			}
			File bakFile = new File(dateFile, file.getOriginalFilename());

			FileUtils.inputstream2file(file.getInputStream(), bakFile);

			GongzidanRender gongzidanRender = read(file);
			if (CollectionUtils.isNotEmpty(gongzidanRender.getGongzidans())) {
				// 加入重复工资单处理删除，所有时间重复的工资单
				this.getService().delByRiqi(gongzidanRender.getRiqi());
				for (Gongzidan Gongzidan : gongzidanRender.getGongzidans()) {
					Gongzidan.setRiqi(gongzidanRender.getRiqi());
					this.add(Gongzidan);
				}
			}
			result.put("status", "success");
		} catch (Exception e) {
			result.put("status", "error");
			e.printStackTrace();
		}
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(JSONObject.toJSON(result).toString());
	}

	private GongzidanRender read(final MultipartFile file) {
		InputStream inputXML = new BufferedInputStream(this.getClass().getClassLoader().getResourceAsStream(xmlConfig));
		XLSReader mainReader;
		try {
			mainReader = ReaderBuilder.buildFromXML(inputXML);

			InputStream inputXLS = new BufferedInputStream(file.getInputStream());
			ReaderConfig.getInstance().setUseDefaultValuesForPrimitiveTypes(true);
			GongzidanRender gongzidanRender = new GongzidanRender();
			Gongzidan gongzidan = new Gongzidan();
			List gongzidans = new ArrayList();
			Map beans = new HashMap();
			beans.put("gongzidanRender", gongzidanRender);
			beans.put("gongzidan", gongzidan);
			beans.put("gongzidanRender.gongzidans", gongzidans);
			XLSReadStatus readStatus = mainReader.read(inputXLS, beans);
			gongzidanRender.setGongzidans(gongzidans);
			return gongzidanRender;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private ExpressionQuery genEmployeeQuery() {
		SecurityContextImpl securityContextImpl = (SecurityContextImpl) getRequest().getSession()
				.getAttribute("SPRING_SECURITY_CONTEXT");

		AuthUser onlineUser = (AuthUser) securityContextImpl.getAuthentication().getPrincipal();

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
