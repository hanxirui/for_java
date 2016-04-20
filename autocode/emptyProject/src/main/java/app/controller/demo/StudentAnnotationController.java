package app.controller.demo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.durcframework.core.controller.CrudController;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import app.entity.SearchStudentEntity;
import app.entity.Student;
import app.service.StudentService;

// 使用注解拼接查询语句,注解定义在SearchStudentEntity类中
@Controller
public class StudentAnnotationController extends CrudController<Student, StudentService> {
	
	@InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
	
	// 查询姓张,学号是NO00000013的学生
	// 注解在SearchStudentEntity类里面
	// @ValueField(column = "name",equal="like")
	// @ValueField(column = "stu_No")
	// ${ctx}/listAnno.do?schName=%E5%BC%A0&schStuNo=NO00000013
	@RequestMapping("/listAnno.do")
	public  ModelAndView listPage(SearchStudentEntity searchStudentEntity) {
		
		return this.list(searchStudentEntity);
	}
	
	// 查询性别为男女的学生,为了测试@ListField注解
	// @ListField(column = "gender"),加了这个注解会拼接成一个in的SQL语句,即
	// schGender in (0,1)
	// ${ctx}/listAnnoGender.do?schGender=0&schGender=1
	@RequestMapping("/listAnnoGender.do")
	public  ModelAndView listAnnoGender(SearchStudentEntity searchStudentEntity) {
		
		return this.list(searchStudentEntity);
	} 
	
	public static void main(String[] args) {
		try {
			System.out.println(URLEncoder.encode("张", "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	
}
