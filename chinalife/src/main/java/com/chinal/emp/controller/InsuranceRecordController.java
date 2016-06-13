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
import com.chinal.emp.entity.BankRecord;
import com.chinal.emp.entity.CustomerBasic;
import com.chinal.emp.entity.Employee;
import com.chinal.emp.entity.InsuranceRecord;
import com.chinal.emp.entity.InsuranceRecordSch;
import com.chinal.emp.entity.Org;
import com.chinal.emp.service.BankRecordService;
import com.chinal.emp.service.CustomerBasicService;
import com.chinal.emp.service.EmployeeService;
import com.chinal.emp.service.InsuranceRecordService;
import com.chinal.emp.service.OrgService;
import com.chinal.emp.util.FileUtils;

@Controller
public class InsuranceRecordController extends BsgridController<InsuranceRecord, InsuranceRecordService> {

	private final static String xmlConfig = "InsuranceRecord.xml";
	private static final String CONTENT_TYPE = "application/vnd.ms-excel;charset=GBK";
	private static final String F_HEADER_ARGU1 = "Content-Disposition";
	private static final String F_HEADER_ARGU2 = "attachment;filename=";

	@Autowired
	private CustomerBasicService customerBasicService;

	@Autowired
	private OrgService orgService;

	@Autowired
	private EmployeeService empService;

	@Autowired
	private BankRecordService bankService;

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

		CustomerBasic beibaoren = new CustomerBasic();
		CustomerBasic shouyiren = new CustomerBasic();

		String tb = insuranceRecord.getToubaorenshenfenzhenghao();
		String bb = insuranceRecord.getBeibaoxianrenshenfenzhenghao();
		String sy = insuranceRecord.getShouyirenshenfenzhenghao();
		if (tb != null && !"".equals(tb)) {
			CustomerBasic toubaoren = new CustomerBasic();
			genToubanren(insuranceRecord, toubaoren);
			ExpressionQuery query = new ExpressionQuery();
			query.addValueExpression(new ValueExpression("t.idcardnum", insuranceRecord.getToubaorenshenfenzhenghao()));
			List<CustomerBasic> cus = customerBasicService.find(query);
			// 客户不存在
			if (cus == null || cus.size() == 0) {
				// 新分配人员不存在
				if (insuranceRecord.getXinfenpeirenyuangonghao() == null
						|| "".equals(insuranceRecord.getXinfenpeirenyuangonghao())) {
					ExpressionQuery empQuery = new ExpressionQuery();
					empQuery.addValueExpression(new ValueExpression("t.code", insuranceRecord.getYewuyuandaima()));
					List<Employee> emp = empService.findSimple(empQuery);
					// 业务员不为公司员工
					if (emp == null || emp.size() == 0) {
						// 根据银行网点和员工对应关系，获得员工信息，，将用专管员信息填充原专管员；新分配人员为空，客户中的客户经理保持为空
						ExpressionQuery bankQuery = new ExpressionQuery();
						bankQuery.addValueExpression(new ValueExpression("t.code", insuranceRecord.getYewuyuandaima()));
						List<BankRecord> banks = bankService.find(bankQuery);
						if (banks != null && banks.size() > 0) {
							BankRecord bank = banks.get(0);
							if (bank.getMzhuanguanyuancode() != null && !"".equals(bank.getMzhuanguanyuancode())) {
								insuranceRecord.setYuanzhuanguanyuan(bank.getMzhuanguanyuan());
								insuranceRecord.setYuanzhuanguanyuangonghao(bank.getMzhuanguanyuancode());
							}
						}

					} else if (emp.size() > 0) {
						// 业务员为公司员工
						Employee employee = emp.get(0);
						insuranceRecord.setYuanzhuanguanyuan(employee.getName());
						insuranceRecord.setYuanzhuanguanyuangonghao(employee.getCode());
						insuranceRecord.setXinfenpeirenyuan(employee.getName());
						insuranceRecord.setXinfenpeirenyuangonghao(employee.getCode());
						toubaoren.setEmpname(employee.getName());
						toubaoren.setEmpcode(employee.getCode());
					}

				} else {
					// 新分配人员存在,将客户的客户经理信息置为新分配人员。
					toubaoren.setEmpname(insuranceRecord.getXinfenpeirenyuan());
					toubaoren.setEmpcode(insuranceRecord.getXinfenpeirenyuangonghao());

				}
				customerBasicService.save(toubaoren);
			} else if (cus.size() > 0) {
				// 客户存在
				CustomerBasic customer = cus.get(0);
				// 新分配人员不存在
				if (insuranceRecord.getXinfenpeirenyuangonghao() == null
						|| "".equals(insuranceRecord.getXinfenpeirenyuangonghao())) {
					insuranceRecord.setXinfenpeirenyuan(customer.getEmpname());
					insuranceRecord.setXinfenpeirenyuangonghao(customer.getEmpcode());
				} else {
					// 新分配人员存在
					// 如果客户经理和新分配人员相同
					if (customer.getEmpcode().equals(insuranceRecord.getXinfenpeirenyuangonghao())) {
						// do nothing
					} else {
						// 客户经理和新分配人员不相同
						customer.setBeizhu("保单中新分配人员和该客户的客户经理信息不一致，请关注。");
						insuranceRecord.setBeizhu("客户的客户经理信息和该保单中新分配人员信息不一致，请关注。");
						customerBasicService.update(customer);
					}
				}

			}

			ExpressionQuery insuranceQuery = new ExpressionQuery();
			insuranceQuery
					.addValueExpression(new ValueExpression("t.baoxiandanhao", insuranceRecord.getBaoxiandanhao()));
			// 保单重复的处理逻辑
			if (this.getService().find(insuranceQuery).size() == 0) {
				this.add(insuranceRecord);
			}
		} else if (bb != null && !"".equals(bb) && !bb.equals(tb)) {
			beibaoren.setName(insuranceRecord.getToubaorenxingming());
			beibaoren.setBirthday(insuranceRecord.getBeibaoxianrenshenfenzhenghao().substring(6, 14));
			beibaoren.setSex(insuranceRecord.getBeibaoxianrenxingbie() != null
					&& "男".equals(insuranceRecord.getBeibaoxianrenxingbie()) ? "1" : "0");
			beibaoren.setAddr(insuranceRecord.getBeibaoxianrentongxundizhi());
			beibaoren.setPhone(insuranceRecord.getBeibaoxianrenshoujihao());
			beibaoren.setIdcardnum(insuranceRecord.getBeibaoxianrenshenfenzhenghao());
			beibaoren.setEmporgcode(insuranceRecord.getJigouhao());
			// beibaoren.setEmporgname(emporgname);
			beibaoren.setEmpname(insuranceRecord.getYewuyuanxingming());
			beibaoren.setEmpcode(insuranceRecord.getYewuyuandaima());
			ExpressionQuery query = new ExpressionQuery();
			query.addValueExpression(new ValueExpression("t.idcardnum", beibaoren.getIdcardnum()));
			int count = customerBasicService.findTotalCount(query);
			if (count == 0) {
				customerBasicService.save(beibaoren);
			}
		} else if (sy != null && !"".equals(sy) && !sy.equals(tb) && !sy.equals(bb)) {
			shouyiren.setName(insuranceRecord.getShouyirenxingming());
			shouyiren.setBirthday(insuranceRecord.getShouyirenshenfenzhenghao().substring(6, 14));
			shouyiren.setSex(
					insuranceRecord.getShouyirenxingbie() != null && "男".equals(insuranceRecord.getShouyirenxingbie())
							? "1" : "0");
			// shouyiren.setAddr(insuranceRecord.getShouyirentongxundizhi());
			// shouyiren.setPhone(insuranceRecord.getShouyirenshoujihao());
			shouyiren.setIdcardnum(insuranceRecord.getShouyirenshenfenzhenghao());
			shouyiren.setEmporgcode(insuranceRecord.getJigouhao());
			// shouyiren.setEmporgname(emporgname);
			shouyiren.setEmpname(insuranceRecord.getYewuyuanxingming());
			shouyiren.setEmpcode(insuranceRecord.getYewuyuandaima());
			ExpressionQuery query = new ExpressionQuery();
			query.addValueExpression(new ValueExpression("t.idcardnum", shouyiren.getIdcardnum()));
			int count = customerBasicService.findTotalCount(query);
			if (count == 0) {
				customerBasicService.save(shouyiren);
			}
		}

	}

	private void genToubanren(InsuranceRecord insuranceRecord, CustomerBasic toubaoren) {
		toubaoren.setName(insuranceRecord.getToubaorenxingming());

		toubaoren.setBirthday(insuranceRecord.getToubaorenshenfenzhenghao().substring(6, 14));
		toubaoren.setSex(
				insuranceRecord.getToubaorenxingbie() != null && "男".equals(insuranceRecord.getToubaorenxingbie()) ? "1"
						: "0");
		toubaoren.setAddr(insuranceRecord.getToubaorentongxundizhi());
		toubaoren.setPhone(insuranceRecord.getToubaorenshoujihao());
		toubaoren.setIdcardnum(insuranceRecord.getToubaorenshenfenzhenghao());
		toubaoren.setEmporgcode(insuranceRecord.getJigouhao());
		String jigouName = getJigouName(insuranceRecord.getJigouhao());
		if (null != jigouName) {
			toubaoren.setEmporgname(jigouName);
		}
		toubaoren.setEmpname(insuranceRecord.getXinfenpeirenyuan());
		toubaoren.setEmpcode(insuranceRecord.getXinfenpeirenyuangonghao());
	}

	private String getJigouName(String jigouhao) {
		ExpressionQuery query = new ExpressionQuery();
		query.addValueExpression(new ValueExpression("t.code", jigouhao));
		List<Org> orgs = orgService.find(query);
		if (orgs != null && orgs.size() > 0) {
			return orgs.get(0).getName();
		} else {
			return null;
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

	public static void main(String[] args) {
		for (int t_i = 0; t_i < 1914; t_i++) {
			if (t_i < 10) {
				System.out.println("12012319491010000" + t_i);
			}
			if (t_i >= 10 && t_i < 100) {
				System.out.println("1201231949101000" + t_i);
			}
			if (t_i >= 100 && t_i < 1000) {
				System.out.println("120123194910100" + t_i);
			}
			if (t_i >= 1000) {
				System.out.println("12012319491010" + t_i);
			}

		}

	}
}
