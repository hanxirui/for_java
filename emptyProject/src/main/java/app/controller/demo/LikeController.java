package app.controller.demo;

import java.util.ArrayList;
import java.util.List;

import org.durcframework.core.GridResult;
import org.durcframework.core.controller.CrudController;
import org.durcframework.core.expression.Expression;
import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.subexpression.LikeDoubleExpression;
import org.durcframework.core.expression.subexpression.LikeRightExpression;
import org.durcframework.core.expression.subexpression.ValueExpression;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import app.entity.SearchStudentEntity;
import app.entity.Student;
import app.service.StudentService;


// like查询
@Controller
public class LikeController extends CrudController<Student, StudentService> {

	
	// 查询手机号开头为133的学生
	@RequestMapping("/likeRightExpression_backuser.do")
	public  @ResponseBody GridResult likeRightExpression() {
		ExpressionQuery query = new ExpressionQuery();
		query.add(new LikeRightExpression("mobile", "133"));
		return this.query(query);
	}
	
	// 查询手机号尾号567的学生
	@RequestMapping("/likeLeftExpression_backuser.do")
	public  @ResponseBody GridResult likeLeftExpression(SearchStudentEntity searchStudentEntity) {
		List<Expression> conditions = new ArrayList<Expression>();
		boolean flag = true;
		// 根据条件判断添加条件
		// 如果true进行模糊查询,否则进行全值查询
		if(flag) { 
			conditions.add(new LikeDoubleExpression("name", "zhangsan"));
		}else{
			conditions.add(new ValueExpression("name", "zhangsan"));
		}
		return this.query(searchStudentEntity,conditions);
	}
	
	// 查询地区为开封的学生
	@RequestMapping("/likeDoubleExpression_backuser.do")
	public  @ResponseBody GridResult likeDoubleExpression() {
		ExpressionQuery query = new ExpressionQuery();
		query.add(new LikeDoubleExpression("address", "开封"));
		return this.query(query);
	}
	
}
