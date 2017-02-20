package app.controller.demo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.durcframework.core.WebContext;
import org.durcframework.core.controller.ExportController;
import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.subexpression.ValueExpression;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import app.entity.SearchStudentEntity;
import app.entity.Student;
import app.service.StudentService;

// 导出功能
// 大数据量导出可能会慢
@Controller
public class ExportExcelController extends ExportController<Student, StudentService> {

	// 模板文件路径
	@Override
	public String getTemplateFilePath() {
		HttpServletRequest request = WebContext.getInstance().getRequest();
		String path = request.getSession().getServletContext().getRealPath("/");
		// X:xxx/main/webapp/webapp/student_export.xls
		// 全路径
		return path + "/student_export.xls";
	}
	
	// ${ctx}/exportStudent_backuser.do
	@RequestMapping("exportStudent_backuser.do")
	public void export(HttpServletResponse response){
		doExport(getList(), response);
	}
	
	// 导出性别为男的,带请求参数
	// ${ctx}/exportStudent2_backuser.do?&schGender=1
	@RequestMapping("exportStudent2_backuser.do")
	public void export2(SearchStudentEntity searchStudentEntity,HttpServletResponse response){
		this.exportBySearchEntity(searchStudentEntity, response);
	}
	
	// 导出性别为女的,服务器端写死
	// ${ctx}/exportStudent3_backuser.do
	@RequestMapping("exportStudent3_backuser.do")
	public void export3(HttpServletResponse response){
		ExpressionQuery query = new ExpressionQuery();
		
		query.setQueryAll(true); // 查询全部
		query.add(new ValueExpression("gender", 0));
		
		this.exportByQuery(query, response);
	}
	
	// 导出的文件名
	@Override
	public String getExportFileName() {
		return "学生信息.xls";
	}
	
	
	
	@Override
	protected Map<String, Object> getModelMap() {
		Map<String, Object> map = super.getModelMap();
		map.put("title", "学生信息");
		return map;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<?> getList(){
		List list = new ArrayList();
		Date date = new Date();
		Student s;
		for (int i = 0; i < 50; i++) {
			s = new Student();
			s.setAddress("address"+i);
			s.setBirthday(date);
			s.setDepartment(16);
			s.setGender((byte)1);
			s.setId(i);
			s.setMobile("mobile"+i);
			s.setName("name"+i);
			s.setNationality("汉族");
			s.setPoliticsStatus(1);
			s.setRegistDate(date);
			s.setStuNo("stuNo"+i);
			
			list.add(s);
		}
		return list;
	}

}
