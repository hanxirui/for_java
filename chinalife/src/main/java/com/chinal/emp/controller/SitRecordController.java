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
import com.chinal.emp.entity.SitRecord;
import com.chinal.emp.entity.SitRecordSch;
import com.chinal.emp.security.AuthUser;
import com.chinal.emp.service.CustomerBasicService;
import com.chinal.emp.service.SitRecordService;

@Controller
public class SitRecordController extends BsgridController<SitRecord, SitRecordService> {
	@Autowired
	private CustomerBasicService customerBasicService;

	@Autowired
	HttpServletRequest request;

	@RequestMapping("/openSitRecordForC.do")
	public ModelAndView openSitRecordForC(String id) {
		ModelAndView mv = new ModelAndView();
		CustomerBasic cus = customerBasicService.get(Integer.parseInt(id));
		mv.addObject("customer", cus);
		mv.setViewName("sitRecordForC");
		return mv;
	}

	@RequestMapping("/openSitRecord.do")
	public String openSitRecord() {
		return "sitRecord";
	}

	@RequestMapping("/addSitRecord.do")
	public ModelAndView addSitRecord(SitRecord entity) {
		SecurityContextImpl securityContextImpl = (SecurityContextImpl) request.getSession()
				.getAttribute("SPRING_SECURITY_CONTEXT");
		AuthUser onlineUser = (AuthUser) securityContextImpl.getAuthentication().getPrincipal();
		entity.setEmpcode(onlineUser.getEmployee().getCode());
		return this.add(entity);
	}

	@RequestMapping("/listSitRecord.do")
	public ModelAndView listSitRecord(SitRecordSch searchEntity, String idcardnum) {
		if (idcardnum != null && !"".equals(idcardnum)) {
			ExpressionQuery query = new ExpressionQuery();
			query.addValueExpression(new ValueExpression("t.idcardnum", idcardnum));
			return this.list(query);
		} else {
			return this.list(searchEntity);
		}

	}

	@RequestMapping("/updateSitRecord.do")
	public ModelAndView updateSitRecord(SitRecord entity) {
		return this.modify(entity);
	}

	@RequestMapping("/delSitRecord.do")
	public ModelAndView delSitRecord(SitRecord entity) {
		return this.remove(entity);
	}

}
