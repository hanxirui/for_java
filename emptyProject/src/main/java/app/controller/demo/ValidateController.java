package app.controller.demo;

import java.util.List;

import org.durcframework.core.MessageResult;
import org.durcframework.core.ValidateHolder;
import org.durcframework.core.controller.BaseController;
import org.durcframework.core.util.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import app.entity.Student;

// 验证
@Controller
public class ValidateController extends BaseController {

	@RequestMapping("/validateStu_backuser.do")
	public @ResponseBody ValidateHolder validateStu(Student student) {
		ValidateHolder result = ValidateUtil.validate(student);
		return result;
	}
	
	@RequestMapping("/validateStu2_backuser.do")
	public @ResponseBody MessageResult validateStu2(Student student) {
		ValidateHolder result = ValidateUtil.validate(student);
		List<String> errors = result.getErrors();
		return this.error("error", errors);
	}

}
