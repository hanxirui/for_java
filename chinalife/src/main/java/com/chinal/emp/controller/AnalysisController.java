package com.chinal.emp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.durcframework.core.controller.BaseController;
import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.subexpression.ValueExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.chinal.emp.entity.AnalysisPojo;
import com.chinal.emp.entity.AnalysisResultPojo;
import com.chinal.emp.service.BizplatformService;
import com.chinal.emp.service.CustomerBasicService;
import com.chinal.emp.service.InsuranceRecordService;
import com.chinal.emp.util.DateUtil;

@Controller
public class AnalysisController extends BaseController {
	@Autowired
	private CustomerBasicService customerBasicService;

	@Autowired
	private BizplatformService bizplatformService;

	@Autowired
	private InsuranceRecordService insuranceRecordService;

	@Autowired
	HttpServletRequest request;

	@RequestMapping("/openBaodanjinequshi.do")
	public ModelAndView openBaodanjinequshi(final AnalysisPojo pojo) throws Exception {
		ModelAndView view = new ModelAndView();
		if (pojo.getStartDate() == null || "".equals(pojo.getStartDate())) {
			pojo.setStartDate(DateUtil.dateAddMonth(DateUtil.getShortFormatNow(), -1));
		}
		if (pojo.getEndDate() == null || "".equals(pojo.getEndDate())) {
			pojo.setEndDate(DateUtil.getShortFormatNow());
		}

		ExpressionQuery query = new ExpressionQuery();

		query.addValueExpression(new ValueExpression("t.toubaoriqi", ">=", pojo.getStartDate()));
		query.addValueExpression(new ValueExpression("t.toubaoriqi", "<=", pojo.getEndDate()));
		query.addSort("t.toubaoriqi", "ASC,group by t.toubaoriqi");
		query.setQueryAll(true);
		query.setPageIndex(1);
		query.setPageSize(1000);
		List<AnalysisResultPojo> datas = insuranceRecordService.queryAnalysisReport(query);
		if (datas.size() > 0) {
			view.addObject("nodata", false);
		} else {
			view.addObject("nodata", true);
		}

		view.addObject("AnalysisResult", JSONObject.toJSONString(datas));
		view.setViewName("analysis/baodanjinequshi");
		return view;
	}

	@RequestMapping("/openBaodanshuliangqushi.do")
	public ModelAndView openBaodanshuliangqushi(final AnalysisPojo pojo) throws Exception {
		ModelAndView view = new ModelAndView();
		if (pojo.getStartDate() == null || "".equals(pojo.getStartDate())) {
			pojo.setStartDate(DateUtil.dateAddMonth(DateUtil.getShortFormatNow(), -1));
		}
		if (pojo.getEndDate() == null || "".equals(pojo.getEndDate())) {
			pojo.setEndDate(DateUtil.getShortFormatNow());
		}

		ExpressionQuery query = new ExpressionQuery();

		query.addValueExpression(new ValueExpression("t.toubaoriqi", ">=", pojo.getStartDate()));
		query.addValueExpression(new ValueExpression("t.toubaoriqi", "<=", pojo.getEndDate()));
		query.addSort("t.toubaoriqi", "ASC,group by t.toubaoriqi");
		query.setQueryAll(true);
		query.setPageIndex(1);
		query.setPageSize(1000);
		List<AnalysisResultPojo> datas = insuranceRecordService.queryAnalysisReport(query);
		if (datas.size() > 0) {
			view.addObject("nodata", false);
		} else {
			view.addObject("nodata", true);
		}

		view.addObject("AnalysisResult", JSONObject.toJSONString(datas));
		view.setViewName("analysis/baodanshuliangqushi");
		return view;
	}

	@RequestMapping("/openBaodanjinetopn.do")
	public ModelAndView openBaodanjinetopn(final AnalysisPojo pojo) throws Exception {
		ModelAndView view = new ModelAndView();
		view.setViewName("analysis/baodanjinetopn");
		return view;
	}

	@RequestMapping("/openBaodanshuliangtopn.do")
	public ModelAndView openBaodanshuliangtopn(final AnalysisPojo pojo) throws Exception {
		ModelAndView view = new ModelAndView();
		view.setViewName("analysis/baodanshuliangtopn");
		return view;
	}

}
