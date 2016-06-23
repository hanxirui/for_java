package com.chinal.emp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.durcframework.core.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.chinal.emp.report.TemplateQuery;
import com.chinal.emp.report.TemplateVo;
import com.chinal.emp.service.BizplatformService;
import com.chinal.emp.service.CustomerBasicService;

@Controller
public class ReportController extends BaseController {
	@Autowired
	private CustomerBasicService customerBasicService;

	@Autowired
	private BizplatformService bizplatformService;

	@Autowired
	HttpServletRequest request;

	@RequestMapping("/openReport.do")
	public ModelAndView openDetailMain(final String resInstId) throws Exception {
		ModelAndView view = new ModelAndView();
		// DetailInfoWebSocket.instance().init();
		// 查询模板
		List<TemplateVo> templates = TemplateQuery.queryAll();
		view.addObject("templates", templates);

		// IResInstBaseService resInstBaseSvc =
		// m_serviceProxy.getService(IResInstBaseService.S_SERVICE_ID);
		// ResInstBasePojo instance = resInstBaseSvc.getById(resInstId);
		// TemplateVo template = TemplateEngine.instance().execute(resInstId);
		// List<LinkUrlVo> navigations = UrlLinkProcessEngine.instance()
		// .execute(TemplateService.instance().readLinkUrl(template.getNavigations()),
		// instance);
		// List<LinkUrlVo> tools = UrlLinkProcessEngine.instance()
		// .execute(TemplateService.instance().readLinkUrl(template.getTools()),
		// instance);
		// List<PorletVo> porlets = template.getPorlets();
		// view.addObject("navigations", navigations);
		// view.addObject("tools", tools);
		// view.addObject("porlets", porlets);
		// view.addObject("instance", instance);
		view.setViewName("report");
		return view;
	}

}
