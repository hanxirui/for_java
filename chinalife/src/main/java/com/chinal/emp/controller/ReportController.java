package com.chinal.emp.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.durcframework.core.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chinal.emp.report.enumeration.TimeRange;
import com.chinal.emp.report.template.TemplateQuery;
import com.chinal.emp.report.template.TemplateService;
import com.chinal.emp.report.vo.PortletVo;
import com.chinal.emp.report.vo.TemplateVo;
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
		// 查询模板
		List<TemplateVo> templates = TemplateQuery.queryAll();
		view.addObject("templates", templates);

		try {
			view.addObject("template", templates.get(0));
		} catch (Exception e) {
			e.printStackTrace();
		}
		view.addObject("timeRanges", TimeRange.values());
		view.setViewName("report");
		return view;
	}

	/**
	 * 更新坐标
	 * 
	 * @param templateId
	 *            templateId
	 * @param json
	 *            坐标数据
	 * @return ModelAndView
	 * @throws Exception
	 *             异常
	 */
	@RequestMapping("/updateCoordinate")
	public ModelAndView updateCoordinate(final String templateId, @RequestBody final String json) throws Exception {
		ModelAndView view = new ModelAndView();
		TemplateVo template = TemplateQuery.query(templateId, null, false);
		List<PortletVo> portlets = template.getPortlets();
		if (StringUtils.isNotBlank(json)) {
			Map<String, JSONObject> map = new HashMap<String, JSONObject>();
			Iterator<Object> iterator = JSONArray.parseArray(json).iterator();
			while (iterator.hasNext()) {
				JSONObject jsonObject = (JSONObject) iterator.next();
				map.put(jsonObject.getString("id"), jsonObject);
			}
			if (MapUtils.isNotEmpty(map)) {
				for (PortletVo portlet : portlets) {
					JSONObject jsonObject = map.get(portlet.getId());
					// 添加portlet占位用的不能更新,保证每次渲染都在最后
					if (!TemplateService.S_PORTLETID_PLACEHOLDER.equals(portlet.getId())
							&& map.containsKey(portlet.getId())) {
						if (jsonObject.containsKey("data-row")) {
							portlet.setX(jsonObject.getIntValue("data-row"));
						}
						if (jsonObject.containsKey("data-col")) {
							portlet.setY(jsonObject.getIntValue("data-col"));
						}
						if (jsonObject.containsKey("data-sizex")) {
							portlet.setWidth(jsonObject.getIntValue("data-sizex"));
						}
						if (jsonObject.containsKey("data-sizey")) {
							portlet.setHeight(jsonObject.getIntValue("data-sizey"));
						}
					}
				}
				TemplateService.instance().saveTemplate(templateId, template);
			}
		}
		return view;
	}

}
