package app.controller.demo;

import org.durcframework.core.controller.SearchController;
import org.durcframework.core.expression.ExpressionQuery;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import app.dao.StudentDao;
import app.entity.Student;

@Controller
public class NoServiceController extends SearchController<Student, StudentDao> {

	@RequestMapping("listNoService.do")
	public  Object listNoService() {
		ExpressionQuery query = new ExpressionQuery();
		return this.getService().find(query);
	}
	
}
