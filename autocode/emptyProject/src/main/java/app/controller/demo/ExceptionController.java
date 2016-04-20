package app.controller.demo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.durcframework.core.controller.SearchController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import app.entity.Student;
import app.service.StudentService;

@Controller
public class ExceptionController extends
		SearchController<Student, StudentService> {

	// 返回异常信息
	@SuppressWarnings("unused")
	@RequestMapping("/exceptionStudent.do")
	public ModelAndView addStudent(Student student) {
		int i = 1 / 0; // 模拟出错
		return this.successView();
	}
	
	@Override
	protected Object exceptionHandler(HttpServletRequest request,
			HttpServletResponse response, Exception e) {
		// 默认实现方式
		//return super.exceptionHandler(request, response, e);
		// 自定义错误返回
		return this.errorView("出错:" + e.getMessage());
	}
}
