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
import org.durcframework.core.support.BsgridController;
import org.durcframework.core.util.DateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.SAXException;

import com.alibaba.fastjson.JSONObject;
import com.chinal.emp.entity.Gongzidan;
import com.chinal.emp.entity.GongzidanSch;
import com.chinal.emp.service.GongzidanService;
import com.chinal.emp.util.FileUtils;

import net.sf.jxls.reader.ReaderBuilder;
import net.sf.jxls.reader.XLSReadStatus;
import net.sf.jxls.reader.XLSReader;

@Controller
public class GongzidanController extends BsgridController<Gongzidan, GongzidanService> {

	private final static String xmlConfig = "GongzidanRecord.xml";
	private static final String CONTENT_TYPE = "application/vnd.ms-excel;charset=GBK";
	private static final String F_HEADER_ARGU1 = "Content-Disposition";
	private static final String F_HEADER_ARGU2 = "attachment;filename=";

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
		return this.list(searchEntity);
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

			List<Gongzidan> list = new ArrayList<Gongzidan>();

			list = read(file);
			if (CollectionUtils.isNotEmpty(list)) {
				for (Gongzidan Gongzidan : list) {
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

	private List<Gongzidan> read(final MultipartFile file) {
		InputStream inputXML = new BufferedInputStream(this.getClass().getClassLoader().getResourceAsStream(xmlConfig));
		XLSReader mainReader;
		try {
			mainReader = ReaderBuilder.buildFromXML(inputXML);
			InputStream inputXLS = new BufferedInputStream(file.getInputStream());// new
																					// FileInputStream(file)
			Gongzidan gongzidan = new Gongzidan();
			List gongzidans = new ArrayList();
			Map beans = new HashMap();
			beans.put("gongzidan", gongzidan);
			beans.put("gongzidans", gongzidans);
			XLSReadStatus readStatus = mainReader.read(inputXLS, beans);
			return gongzidans;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
