package app.controller.demo;

import java.util.HashMap;
import java.util.Map;

import org.durcframework.core.GridResult;
import org.durcframework.core.MessageResult;
import org.durcframework.core.controller.CrudController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import app.entity.SearchStudentEntity;
import app.entity.Student;
import app.service.StudentService;

// 返回结果
@Controller
public class ViewController extends CrudController<Student, StudentService> {

	@RequestMapping("listMav_backuser.do")
	public @ResponseBody GridResult listMav(SearchStudentEntity entity) {
		return this.query(entity);
	}
	
	@RequestMapping("viewSuccess_backuser.do")
	public @ResponseBody MessageResult viewSuccess() {
		return this.success("成功");
	}
	
	@RequestMapping("viewError_backuser.do")
	public @ResponseBody MessageResult viewError() {
		return this.error("失败");
	}
	
	@RequestMapping("viewMap_backuser.do")
	public @ResponseBody Map<String, Object> viewMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("aa", 1);
		map.put("bb", "aaa");
		return map;
	}
	
	
	
}
