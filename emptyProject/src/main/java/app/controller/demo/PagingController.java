package app.controller.demo;

import org.durcframework.core.GridResult;
import org.durcframework.core.controller.CrudController;
import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.subexpression.ValueExpression;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import app.entity.Student;
import app.service.StudentService;

// 分页相关
@Controller
public class PagingController extends CrudController<Student, StudentService> {

	
	// 传统方式分页
	@RequestMapping("/listPage2_backuser.do")
	public  @ResponseBody GridResult listPage2() {
		ExpressionQuery query = new ExpressionQuery();
		query.setPageIndex(1) // 第一页
			.setPageSize(20); // 每页显示20条
		
		// 添加排序
		query.addSort("regist_date","desc");
		
		return this.query(query);
	}
	
	// 类似mysql形式分页
	@RequestMapping("/listPage_backuser.do")
	public  @ResponseBody GridResult listPage() {
		ExpressionQuery query = new ExpressionQuery();
		// 第一页
		query.setStart(0);
		// 每页显示10条
		query.setLimit(10);
		
		// 筛选性别
		query.add(new ValueExpression("gender",1));
		// 添加排序
		query.addSort("regist_date","desc");
		
		return this.query(query);
	}
	
}
