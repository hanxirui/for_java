package app.controller.demo;

import org.durcframework.core.controller.CrudController;
import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.subexpression.LikeDoubleExpression;
import org.durcframework.core.expression.subexpression.LikeLeftExpression;
import org.durcframework.core.expression.subexpression.LikeRightExpression;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import app.entity.Student;
import app.service.StudentService;


// like查询
@Controller
public class StudentLikeController extends CrudController<Student, StudentService> {

	
	// ${ctx}/likeRightExpression.do
	// 查询手机号开头为133的学生
	@RequestMapping("/likeRightExpression.do")
	public  ModelAndView likeRightExpression() {
		ExpressionQuery query = new ExpressionQuery();
		query.add(new LikeRightExpression("mobile", "133"));
		return this.list(query);
	}
	
	// ${ctx}/likeLeftExpression.do
	// 查询手机号尾号567的学生
	@RequestMapping("/likeLeftExpression.do")
	public  ModelAndView likeLeftExpression() {
		ExpressionQuery query = new ExpressionQuery();
		query.add(new LikeLeftExpression("mobile", "567"));
		return this.list(query);
	}
	
	// ${ctx}/likeDoubleExpression.do
	// 查询地区为开封的学生
	@RequestMapping("/likeDoubleExpression.do")
	public  ModelAndView likeDoubleExpression() {
		ExpressionQuery query = new ExpressionQuery();
		query.add(new LikeDoubleExpression("address", "开封"));
		return this.list(query);
	}
	
}
