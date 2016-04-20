package app.controller.demo;

import java.util.List;

import org.durcframework.core.ValidateHolder;
import org.durcframework.core.controller.BaseController;
import org.durcframework.core.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import app.entity.Student;

// 验证
@Controller
public class ValidateController extends BaseController {

	@RequestMapping("/validateStu.do")
	public ModelAndView validateStu(Student student) {
		ValidateHolder result = ValidateUtil.validate(student);
		return this.render(result);
	}
	
	@RequestMapping("/validateStu2.do")
	public ModelAndView validateStu2(Student student) {
		ValidateHolder result = ValidateUtil.validate(student);
		List<String> errors = result.getErrors();
		return this.errorView("error", errors);
	}

}
