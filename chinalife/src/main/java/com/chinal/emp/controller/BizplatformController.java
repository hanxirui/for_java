package com.chinal.emp.controller;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.durcframework.core.support.BsgridController;
import org.durcframework.core.util.DateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.chinal.emp.entity.Bizplatform;
import com.chinal.emp.entity.BizplatformSch;
import com.chinal.emp.service.BizplatformService;
import com.chinal.emp.util.FileUtils;

@Controller
public class BizplatformController extends BsgridController<Bizplatform, BizplatformService> {

	@RequestMapping("/openBizplatform.do")
	public String openBizplatform() {
		return "bizplatform";
	}

	@RequestMapping("/openBizPlatformDetail.do")
	public ModelAndView openBizPlatformDetail() {
		ModelAndView mv = new ModelAndView();
		String date = DateUtil.format(new Date(), "yyyyMM");
		List<String> files = FileUtils.listFiles(new File(this.getRequest().getRealPath("/") + File.separator + date));
		mv.addObject("path", date);
		mv.addObject("bizfile", files);
		mv.setViewName("bizplatformDetail");
		return mv;
	}

	@RequestMapping("/openBizCalendar.do")
	public String getBizEvents() {
		return "bizCalendar";
	}

	@RequestMapping("/listFile.do")
	public void listFile(HttpServletRequest request, HttpServletResponse response) throws Exception {

		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(JSONObject.toJSON("").toString());
	}

	@RequestMapping("/addBizplatform.do")
	public ModelAndView addBizplatform(Bizplatform entity) {
		return this.add(entity);
	}

	@RequestMapping("/listBizplatform.do")
	public ModelAndView listBizplatform(BizplatformSch searchEntity) {
		return this.list(searchEntity);
	}

	@RequestMapping("/updateBizplatform.do")
	public ModelAndView updateBizplatform(Bizplatform entity) {
		return this.modify(entity);
	}

	@RequestMapping("/delBizplatform.do")
	public ModelAndView delBizplatform(Bizplatform entity) {
		return this.remove(entity);
	}

}
