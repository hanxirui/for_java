package com.chinal.emp.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import org.durcframework.core.util.DateUtil;
import org.jxls.reader.ReaderBuilder;
import org.jxls.reader.XLSReadStatus;
import org.jxls.reader.XLSReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.SAXException;

import com.alibaba.fastjson.JSONObject;
import com.chinal.emp.entity.CustomerBasic;
import com.chinal.emp.entity.InsuranceRecord;
import com.chinal.emp.entity.InsuranceRecordSch;
import com.chinal.emp.service.CustomerBasicService;
import com.chinal.emp.service.InsuranceRecordService;
import com.chinal.emp.util.FileUtils;

@Controller
public class InsuranceRecordController extends BsgridController<InsuranceRecord, InsuranceRecordService> {

	private final static String xmlConfig = "InsuranceRecord.xml";
	private static final String CONTENT_TYPE = "application/vnd.ms-excel;charset=GBK";
	private static final String F_HEADER_ARGU1 = "Content-Disposition";
	private static final String F_HEADER_ARGU2 = "attachment;filename=";

	@Autowired
	private CustomerBasicService customerBasicService;

	@RequestMapping("/openInsuranceForC.do")
	public ModelAndView openInsuranceForC(String id) {
		ModelAndView mv = new ModelAndView();
		CustomerBasic cus = customerBasicService.get(Integer.parseInt(id));
		mv.addObject("customer", cus);
		mv.setViewName("insuranceRecordForC");
		return mv;
	}

	@RequestMapping("/openInsuranceRecord.do")
	public String openInsuranceRecord() {
		return "insuranceRecord";
	}

	@RequestMapping("/addInsuranceRecord.do")
	public ModelAndView addInsuranceRecord(InsuranceRecord entity) {
		return this.add(entity);
	}

	@RequestMapping("/listInsuranceRecord.do")
	public ModelAndView listInsuranceRecord(InsuranceRecordSch searchEntity, String idcardnum) {
		if (idcardnum != null && !"".equals(idcardnum)) {
			searchEntity.setToubaorenshenfenzhenghao(idcardnum);
		}
		return this.list(searchEntity);
	}

	@RequestMapping("/updateInsuranceRecord.do")
	public ModelAndView updateInsuranceRecord(InsuranceRecord entity) {
		return this.modify(entity);
	}

	@RequestMapping("/delInsuranceRecord.do")
	public ModelAndView delInsuranceRecord(InsuranceRecord entity) {
		return this.remove(entity);
	}

	@RequestMapping("/importInsurance.do")
	public void importInsurance(HttpServletRequest request, HttpServletResponse response) throws Exception {
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

			List<InsuranceRecord> list = new ArrayList<InsuranceRecord>();

			list = read(file);
			if (CollectionUtils.isNotEmpty(list)) {
				for (InsuranceRecord insuranceRecord : list) {
					ExpressionQuery query = new ExpressionQuery();
					query.addValueExpression(
							new ValueExpression("t.baoxiandanhao", insuranceRecord.getBaoxiandanhao()));
					if (this.getService().find(query).size() == 0) {
						this.add(insuranceRecord);
					}
					addCustomerBasic(insuranceRecord);
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

	private void addCustomerBasic(InsuranceRecord insuranceRecord) {
		CustomerBasic toubaoren = new CustomerBasic();
		CustomerBasic beibaoren = new CustomerBasic();
		CustomerBasic shouyiren = new CustomerBasic();

		String tb = insuranceRecord.getToubaorenshenfenzhenghao();
		String bb = insuranceRecord.getBeibaoxianrenshenfenzhenghao();
		String sy = insuranceRecord.getShouyirenshenfenzhenghao();
		if (tb != null && !"".equals(tb)) {
			toubaoren.setName(insuranceRecord.getToubaorenxingming());
			toubaoren.setBirthday(insuranceRecord.getToubaorenshenfenzhenghao().substring(6, 14));
			toubaoren.setSex(insuranceRecord.getToubaorenxingbie());
			toubaoren.setAddr(insuranceRecord.getToubaorentongxundizhi());
			toubaoren.setPhone(insuranceRecord.getToubaorenshoujihao());
			toubaoren.setIdcardnum(insuranceRecord.getToubaorenshenfenzhenghao());
			toubaoren.setEmporgcode(insuranceRecord.getJigouhao());
			// toubaoren.setEmporgname(emporgname);
			toubaoren.setEmpname(insuranceRecord.getYewuyuanxingming());
			toubaoren.setKehujingli(insuranceRecord.getYewuyuandaima());
			ExpressionQuery query = new ExpressionQuery();
			query.addValueExpression(new ValueExpression("t.idcardnum", toubaoren.getIdcardnum()));
			int count = customerBasicService.findTotalCount(query);
			if (count == 0) {
				customerBasicService.save(toubaoren);
			}
		} else if (bb != null && !"".equals(bb) && !bb.equals(tb)) {
			beibaoren.setName(insuranceRecord.getToubaorenxingming());
			beibaoren.setBirthday(insuranceRecord.getBeibaoxianrenshenfenzhenghao().substring(6, 14));
			beibaoren.setSex(insuranceRecord.getBeibaoxianrenxingbie());
			beibaoren.setAddr(insuranceRecord.getBeibaoxianrentongxundizhi());
			beibaoren.setPhone(insuranceRecord.getBeibaoxianrenshoujihao());
			beibaoren.setIdcardnum(insuranceRecord.getBeibaoxianrenshenfenzhenghao());
			beibaoren.setEmporgcode(insuranceRecord.getJigouhao());
			// beibaoren.setEmporgname(emporgname);
			beibaoren.setEmpname(insuranceRecord.getYewuyuanxingming());
			beibaoren.setKehujingli(insuranceRecord.getYewuyuandaima());
			ExpressionQuery query = new ExpressionQuery();
			query.addValueExpression(new ValueExpression("t.idcardnum", beibaoren.getIdcardnum()));
			int count = customerBasicService.findTotalCount(query);
			if (count == 0) {
				customerBasicService.save(beibaoren);
			}
		} else if (sy != null && !"".equals(sy) && !sy.equals(tb) && !sy.equals(bb)) {
			shouyiren.setName(insuranceRecord.getShouyirenxingming());
			shouyiren.setBirthday(insuranceRecord.getShouyirenshenfenzhenghao().substring(6, 14));
			shouyiren.setSex(insuranceRecord.getShouyirenxingbie());
			// shouyiren.setAddr(insuranceRecord.getShouyirentongxundizhi());
			// shouyiren.setPhone(insuranceRecord.getShouyirenshoujihao());
			shouyiren.setIdcardnum(insuranceRecord.getShouyirenshenfenzhenghao());
			shouyiren.setEmporgcode(insuranceRecord.getJigouhao());
			// shouyiren.setEmporgname(emporgname);
			shouyiren.setEmpname(insuranceRecord.getYewuyuanxingming());
			shouyiren.setKehujingli(insuranceRecord.getYewuyuandaima());
			ExpressionQuery query = new ExpressionQuery();
			query.addValueExpression(new ValueExpression("t.idcardnum", shouyiren.getIdcardnum()));
			int count = customerBasicService.findTotalCount(query);
			if (count == 0) {
				customerBasicService.save(shouyiren);
			}
		}

	}

	private List<InsuranceRecord> read(final MultipartFile file) {
		InputStream inputXML = new BufferedInputStream(this.getClass().getClassLoader().getResourceAsStream(xmlConfig));
		XLSReader mainReader;
		try {
			mainReader = ReaderBuilder.buildFromXML(inputXML);
			InputStream inputXLS = new BufferedInputStream(file.getInputStream());// new
																					// FileInputStream(file)
			InsuranceRecord insurance = new InsuranceRecord();
			List insurances = new ArrayList();
			Map beans = new HashMap();
			beans.put("insurance", insurance);
			beans.put("insurances", insurances);
			XLSReadStatus readStatus = mainReader.read(inputXLS, beans);
			return insurances;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping("/exportUserExcel.do")
	public void exportUserExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> beans = new HashMap<String, Object>();
		String exportFile = "userlist.xlsx";
		response.setHeader(F_HEADER_ARGU1, F_HEADER_ARGU2 + exportFile);
		response.setContentType(CONTENT_TYPE); // "application/vnd.ms-excel"
		OutputStream os = null;// new FileOutputStream(exportFile);
		InputStream is = null;
		String templateFile = "userTemp.xlsx";
		try {
			List<Map<String, String>> userList = new ArrayList<Map<String, String>>();
			// List<User> listAll = userService.getAllList();
			// if (CollectionUtils.isNotEmpty(listAll)) {
			// for (User user : listAll) {
			// Map<String, String> exel = new HashMap<String, String>();
			// exel.put("usercode", null == user.getUsercode() ? null :
			// user.getUsercode());
			// exel.put("username", null == user.getUsername() ? null :
			// user.getUsername());
			// exel.put("college", null == user.getCollege() ? null :
			// user.getCollege());
			// exel.put("classes", null == user.getClasses() ? null :
			// user.getClasses());
			// exel.put("usermac", null == user.getUsermac() ? null :
			// user.getUsermac());
			// exel.put("usertel", null == user.getUsertel() ? null :
			// user.getUsertel());
			// exel.put("email", null == user.getEmail() ? null :
			// user.getEmail());
			// userList.add(exel);
			// }
			// beans.put("list", userList);
			// XLSTransformer transformer = new XLSTransformer();
			// is = new
			// BufferedInputStream(this.getClass().getClassLoader().getResourceAsStream(templateFile));
			// Workbook workbook = transformer.transformXLS(is, beans);
			// os = response.getOutputStream();
			// workbook.write(os);
			// }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
	}

	@RequestMapping("/delInsurances.do")
	public ModelAndView delInsurances(String[] ids) {
		this.getService().delInsurances(ids);
		return this.successView();
	}

}
