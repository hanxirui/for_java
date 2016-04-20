package app.controller.demo;

import java.util.HashMap;
import java.util.Map;

import org.durcframework.core.controller.CrudController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import app.entity.SearchStudentEntity;
import app.entity.Student;
import app.service.StudentService;

@Controller
public class ViewController extends CrudController<Student, StudentService> {

	@RequestMapping("listMav.do")
	public ModelAndView listMav(SearchStudentEntity entity) {
		return this.list(entity);
	}
	
	@RequestMapping("viewSuccess.do")
	public ModelAndView viewSuccess() {
		return this.successView("成功");
	}
	
	@RequestMapping("viewError.do")
	public ModelAndView viewError() {
		return this.errorView("失败");
	}
	
	@RequestMapping("viewMap.do")
	public ModelAndView viewMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("aa", 1);
		map.put("bb", "aaa");
		return this.render(map);
	}
	
	
	
}
