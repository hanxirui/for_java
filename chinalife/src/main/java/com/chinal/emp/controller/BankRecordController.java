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
import org.durcframework.core.expression.subexpression.ValueExpression;
import org.durcframework.core.support.BsgridController;
import org.jxls.reader.ReaderBuilder;
import org.jxls.reader.XLSReadStatus;
import org.jxls.reader.XLSReader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.SAXException;

import com.alibaba.fastjson.JSONObject;
import com.chinal.emp.entity.BankRecord;
import com.chinal.emp.entity.BankRecordSch;
import com.chinal.emp.service.BankRecordService;
import com.chinal.emp.util.DateUtil;
import com.chinal.emp.util.FileUtils;

@Controller
public class BankRecordController extends BsgridController<BankRecord, BankRecordService> {

	private final static String xmlConfig = "BankRecord.xml";

	@RequestMapping("/openBankRecord.do")
	public String openBankRecord() {
		return "bankRecord";
	}

	@RequestMapping("/addBankRecord.do")
	public ModelAndView addBankRecord(BankRecord entity) {
		return this.add(entity);
	}

	@RequestMapping("/listBankRecord.do")
	public ModelAndView listBankRecord(BankRecordSch searchEntity) {
		return this.list(searchEntity);
	}

	@RequestMapping("/updateBankRecord.do")
	public ModelAndView updateBankRecord(BankRecord entity) {
		return this.modify(entity);
	}

	@RequestMapping("/delBankRecord.do")
	public ModelAndView delBankRecord(BankRecord entity) {
		return this.remove(entity);
	}

	@RequestMapping("/importBank.do")
	public void importBank(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			MultipartHttpServletRequest mulRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = mulRequest.getFile("filename");

			String date = DateUtil.format(new Date(), "yyyyMM");
			File dateFile = new File(request.getRealPath("/") + File.separator + date);
			if (!dateFile.exists()) {
				dateFile.mkdir();
			}
			File bakFile = new File(dateFile, file.getOriginalFilename());

			FileUtils.inputstream2file(file.getInputStream(), bakFile);

			List<BankRecord> list = new ArrayList<BankRecord>();

			list = read(file);
			if (CollectionUtils.isNotEmpty(list)) {

				for (BankRecord bank : list) {
					ExpressionQuery query = new ExpressionQuery();
					query.addValueExpression(new ValueExpression("t.wangdiancode", bank.getWangdiancode()));
					List<BankRecord> bs = this.getService().find(query);
					if (bs.size() < 1) {
						this.add(bank);
					}
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

	public List<BankRecord> read(final MultipartFile file) {
		InputStream inputXML = new BufferedInputStream(this.getClass().getClassLoader().getResourceAsStream(xmlConfig));
		XLSReader mainReader;
		try {
			mainReader = ReaderBuilder.buildFromXML(inputXML);
			InputStream inputXLS = new BufferedInputStream(file.getInputStream());
			BankRecord bank = new BankRecord();
			List banks = new ArrayList();
			Map beans = new HashMap();
			beans.put("bank", bank);
			beans.put("banks", banks);
			XLSReadStatus readStatus = mainReader.read(inputXLS, beans);
			return banks;
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
