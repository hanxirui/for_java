package com.chinal.emp.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.subexpression.ValueExpression;
import org.durcframework.core.support.BsgridController;
import org.durcframework.core.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.chinal.emp.entity.BizRecord;
import com.chinal.emp.entity.Bizplatform;
import com.chinal.emp.entity.BizplatformSch;
import com.chinal.emp.security.AuthUser;
import com.chinal.emp.service.BizRecordService;
import com.chinal.emp.service.BizplatformService;
import com.chinal.emp.util.FileUtils;

@Controller
public class BizplatformController extends BsgridController<Bizplatform, BizplatformService> {

	protected static Logger logger = Logger.getLogger("controller");

	@Autowired
	HttpServletRequest request;

	@Autowired
	BizRecordService bizRecordService;

	@RequestMapping("/openBizplatform.do")
	public String openBizplatform() {
		return "bizplatform";
	}

	@RequestMapping("/openBizPlatformDetail.do")
	public ModelAndView openBizPlatformDetail(HttpServletRequest request, HttpServletResponse response, String nian,
			String ji) {
		ModelAndView mv = new ModelAndView();
		String date = nian + ji;
		if (nian == null || ji == null || "".equals(nian) || "".equals(ji)) {
			date = DateUtil.format(new Date(), "yyyyMM");
		}
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
		entity.setOrgcode(getOnlineUser().getEmployee().getOrgcode());
		ModelAndView mv = this.add(entity);

		List<String> times = entity.getTimes();
		List<String> noons = entity.getNoons();
		for (int t_i = 0; t_i < times.size() && t_i < noons.size(); t_i++) {
			if (!"".equals(times.get(t_i)) && !"".equals(noons.get(t_i))) {
				BizRecord br = new BizRecord();
				br.setBizplatId(entity.getId());
				br.setBizplatTitle(entity.getTitle() + "(" + noons.get(t_i) + ")");
				br.setRiqi(times.get(t_i));
				bizRecordService.save(br);
			}

		}

		return mv;
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

	// 为平台行事历查询所有的业务平台，只能查询同一部门的和当前日期的
	@RequestMapping("/listBizPlatformForCal.do")
	public ModelAndView listBizPlatformForCal(String date, String empid) {

		ExpressionQuery query = new ExpressionQuery();
		query.addValueExpression(new ValueExpression("t.startdate", "<=", date));
		query.addValueExpression(new ValueExpression("t.enddate", ">=", date));

		if (empid == null || "".equals(empid) || "undefined".equals(empid)) {
			SecurityContextImpl securityContextImpl = (SecurityContextImpl) getRequest().getSession()
					.getAttribute("SPRING_SECURITY_CONTEXT");
			AuthUser onlineUser = (AuthUser) securityContextImpl.getAuthentication().getPrincipal();
			empid = onlineUser.getCode();
		}
		return this.list(query);
	}

	@RequestMapping("/uploadPlatform.do")
	public void importCustomer(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			MultipartHttpServletRequest mulRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = mulRequest.getFile("filename");
			String platid = mulRequest.getParameter("platid");
			String filetype = mulRequest.getParameter("filetype");

			File firstDir = new File(request.getRealPath("/") + File.separator + "platform");
			if (!firstDir.exists()) {
				firstDir.mkdir();
			}
			File secondDir = new File(firstDir, platid);
			if (!secondDir.exists()) {
				secondDir.mkdir();
			}
			File thirdDir = new File(secondDir, filetype);
			if (!thirdDir.exists()) {
				thirdDir.mkdir();
			}
			File bakFile = new File(thirdDir, file.getOriginalFilename());

			FileUtils.inputstream2file(file.getInputStream(), bakFile);

			if (platid != null && !"".equals(platid)) {
				Bizplatform plat = this.get(Integer.valueOf(platid));
				String relativePath = file.getOriginalFilename();
				if ("caiye".equals(filetype)) {
					if (plat.getCaiye() != null) {
						plat.setCaiye(plat.getCaiye() + "," + relativePath);
					} else {
						plat.setCaiye(relativePath);
					}

				} else if ("huashu".equals(filetype)) {

					if (plat.getHuashu() != null) {
						plat.setHuashu(plat.getHuashu() + "," + relativePath);
					} else {
						plat.setHuashu(relativePath);
					}

				} else if ("jishuziliao".equals(filetype)) {

					if (plat.getJishuziliao() != null) {
						plat.setJishuziliao(plat.getJishuziliao() + "," + relativePath);
					} else {
						plat.setJishuziliao(relativePath);
					}

				} else if ("others".equals(filetype)) {

					if (plat.getOthers() != null) {
						plat.setOthers(plat.getOthers() + "," + relativePath);
					} else {
						plat.setOthers(relativePath);
					}

				}
				this.modify(plat);
			}

			result.put("status", "success");
		} catch (Exception e) {
			result.put("status", "error");
			e.printStackTrace();
		}
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(JSONObject.toJSON(result).toString());
	}

	private AuthUser getOnlineUser() {
		SecurityContextImpl securityContextImpl = (SecurityContextImpl) getRequest().getSession()
				.getAttribute("SPRING_SECURITY_CONTEXT");
		AuthUser onlineUser = (AuthUser) securityContextImpl.getAuthentication().getPrincipal();
		return onlineUser;
	}
}
