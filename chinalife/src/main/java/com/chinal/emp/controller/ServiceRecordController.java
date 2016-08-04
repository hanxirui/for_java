package com.chinal.emp.controller;

import javax.servlet.http.HttpServletRequest;

import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.subexpression.ValueExpression;
import org.durcframework.core.support.BsgridController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.chinal.emp.entity.CustomerBasic;
import com.chinal.emp.entity.ServiceRecord;
import com.chinal.emp.entity.ServiceRecordSch;
import com.chinal.emp.security.AuthUser;
import com.chinal.emp.service.BizplatformService;
import com.chinal.emp.service.CustomerBasicService;
import com.chinal.emp.service.ServiceRecordService;

@Controller
public class ServiceRecordController extends BsgridController<ServiceRecord, ServiceRecordService> {
	@Autowired
	private CustomerBasicService customerBasicService;

	@Autowired
	private BizplatformService bizplatformService;

	@Autowired
	HttpServletRequest request;

	@RequestMapping("/openServiceRecordForC.do")
	public ModelAndView openServiceRecordForC(String id) {
		ModelAndView mv = new ModelAndView();
		CustomerBasic cus = customerBasicService.get(Integer.parseInt(id));
		mv.addObject("customer", cus);
		mv.setViewName("serviceRecordForC");
		return mv;
	}

	@RequestMapping("/openServiceRecord.do")
	public ModelAndView openServiceRecord(String id) {
		ModelAndView mv = new ModelAndView();
		CustomerBasic cus = customerBasicService.get(Integer.parseInt(id));
		mv.addObject("customer", cus);
		mv.setViewName("serviceRecord");
		return mv;
	}

	@RequestMapping("/addServiceRecord.do")
	public ModelAndView addServiceRecord(ServiceRecord entity) {
		SecurityContextImpl securityContextImpl = (SecurityContextImpl) request.getSession()
				.getAttribute("SPRING_SECURITY_CONTEXT");
		AuthUser onlineUser = (AuthUser) securityContextImpl.getAuthentication().getPrincipal();
		entity.setEmpcode(onlineUser.getEmployee().getCode());
		return this.add(entity);
	}

	// @RequestMapping("/batchAddServiceRecord.do")
	// public ModelAndView batchAddServiceRecord(String[] cusIds, String platId)
	// {
	// SecurityContextImpl securityContextImpl = (SecurityContextImpl)
	// request.getSession()
	// .getAttribute("SPRING_SECURITY_CONTEXT");
	// AuthUser onlineUser = (AuthUser)
	// securityContextImpl.getAuthentication().getPrincipal();
	// if (cusIds != null && platId != null && cusIds.length > 0 &&
	// !"".equals(platId)) {
	// Bizplatform plat = bizplatformService.get(Integer.parseInt(platId));
	// for (String t_cusid : cusIds) {
	// CustomerBasic cus = customerBasicService.get(Integer.parseInt(t_cusid));
	//
	// ServiceRecord entity = new ServiceRecord();
	// entity.setEmpcode(onlineUser.getEmployee().getCode());
	// entity.setEmpname(onlineUser.getEmployee().getName());
	// entity.setContent(plat.getTitle());
	// entity.setIdcardnum(cus.getIdcardnum());
	// entity.setName(cus.getName());
	// entity.setServicetime(DateUtil.getShortFormatNow());
	// entity.setType(plat.getZhishibaifang());
	//
	// this.add(entity);
	// }
	// }
	//
	// return this.render("success");
	// }

	@RequestMapping("/listServiceRecord.do")
	public ModelAndView listServiceRecord(ServiceRecordSch searchEntity, String idcardnum) {
		if (idcardnum != null && !"".equals(idcardnum)) {
			ExpressionQuery query = new ExpressionQuery();
			query.addValueExpression(new ValueExpression("t.idcardnum", idcardnum));

			query.setPageSize(searchEntity.getPageSize());
			query.setPageIndex(searchEntity.getPageIndex());
			return this.list(query);
		} else {
			return this.list(searchEntity);
		}

	}

	@RequestMapping("/updateServiceRecord.do")
	public ModelAndView updateServiceRecord(ServiceRecord entity) {
		return this.modify(entity);
	}

	@RequestMapping("/delServiceRecord.do")
	public ModelAndView delServiceRecord(ServiceRecord entity) {
		return this.remove(entity);
	}

}
