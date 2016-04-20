package app.controller.demo;

import java.util.ArrayList;
import java.util.List;

import org.durcframework.core.controller.SearchController;
import org.durcframework.core.expression.Expression;
import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.subexpression.LikeRightExpression;
import org.durcframework.core.expression.subexpression.ValueExpression;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import app.entity.SearchStudentEntity;
import app.entity.Student;
import app.service.StudentService;

@Controller
public class StudentListExpresstionController extends
		SearchController<Student, StudentService> {

	// 查询名为JIM并且手机号133开头的学生
	// ${ctx}/listExpresstions.do
	@RequestMapping("/listExpresstions.do")
	public  ModelAndView listExpresstions(SearchStudentEntity entity) {
		// 追加条件
		List<Expression> expressions = new ArrayList<Expression>();
		expressions.add(new ValueExpression("name", "JIM"));
		expressions.add(new LikeRightExpression("mobile", "133"));
		
		return this.list(entity,expressions);
	}
	
	// 查询名为JIM并且手机号133开头的学生,方式2
	// ${ctx}/listExpresstions.do
	@RequestMapping("/listExpresstions2.do")
	public  ModelAndView listExpresstions2() {
		ExpressionQuery query = new ExpressionQuery();
		// 追加条件
		List<Expression> expressions = new ArrayList<Expression>();
		expressions.add(new ValueExpression("name", "JIM"));
		expressions.add(new LikeRightExpression("mobile", "133"));
		
		query.addAll(expressions);
		
		return this.list(query);
	}
}
