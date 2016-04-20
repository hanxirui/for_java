package app.controller.demo;

import org.durcframework.core.SearchPojo;
import org.durcframework.core.controller.SearchController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import app.entity.ExtStudentSearch;
import app.entity.Student;
import app.service.StudentService;

/**
 * 支持Extjs查询
 * @author hc.tang
 *
 */
@Controller
public class ExtjsPagingController extends SearchController<Student, StudentService> {
	
	// ${ctx}/listExtjsPagging.do?start=0&limit=10
	@RequestMapping("listExtjsPagging.do")
	public  ModelAndView listExtjsPagging(SearchPojo searchEntity) {
		return this.list(searchEntity);
	}
	
	// ${ctx}/listExtjsPagging2.do?start=3&limit=5&schName=jim
	@RequestMapping("listExtjsPagging2.do")
	public  ModelAndView listExtjsPagging2(ExtStudentSearch searchEntity) {
		return this.list(searchEntity);
	}
}
