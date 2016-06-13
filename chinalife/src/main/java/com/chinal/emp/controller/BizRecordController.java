package com.chinal.emp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.subexpression.LikeRightExpression;
import org.durcframework.core.expression.subexpression.ValueExpression;
import org.durcframework.core.support.BsgridController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.chinal.emp.entity.BizRecord;
import com.chinal.emp.entity.BizRecordSch;
import com.chinal.emp.security.AuthUser;
import com.chinal.emp.service.BizRecordService;

@Controller
public class BizRecordController extends BsgridController<BizRecord, BizRecordService> {

	@Autowired
	HttpServletRequest request;

	@RequestMapping("/openBizRecord.do")
	public String openBizRecord() {
		return "bizRecord";
	}

	@RequestMapping("/getEventMonth.do")
	public ModelAndView getEventMonth(String month) {
		ExpressionQuery query = new ExpressionQuery();

		query.addValueExpression(new LikeRightExpression("t.riqi", month));

		// 返回查询结果
		return this.list(query);
	}

	@RequestMapping("/getEventDay.do")
	public ModelAndView getEventDay(String day) {
		ExpressionQuery query = new ExpressionQuery();

		query.addValueExpression(new ValueExpression("t.riqi", day));

		// 返回查询结果
		return this.list(query);
	}

	@RequestMapping("/addBizRecord.do")
	public ModelAndView addBizRecord(BizRecord entity) {
		SecurityContextImpl securityContextImpl = (SecurityContextImpl) request.getSession()
				.getAttribute("SPRING_SECURITY_CONTEXT");
		AuthUser onlineUser = (AuthUser) securityContextImpl.getAuthentication().getPrincipal();
		entity.setKehujingli(onlineUser.getCode());
		return this.add(entity);
	}

	@RequestMapping("/listBizRecord.do")
	public ModelAndView listBizRecord(BizRecordSch searchEntity) {
		return this.list(searchEntity);
	}

	@RequestMapping("/updateBizRecord.do")
	public ModelAndView updateBizRecord(BizRecord entity) {
		return this.modify(entity);
	}

	@RequestMapping("/delBizRecord.do")
	public ModelAndView delBizRecord(BizRecord entity) {
		return this.remove(entity);
	}

	@RequestMapping("/getBizplatTongjiInfo.do")
	public ModelAndView getBizplatTongjiInfo(String currMonth) {
		ModelAndView mv = new ModelAndView();
		List<BizRecord> records = this.getService().getTongjiInfo(currMonth);
		BizRecord record = new BizRecord();
		if (records != null && records.size() > 0) {
			record = records.get(0);
		}
		mv = this.render(record);
		return mv;
	}

}
