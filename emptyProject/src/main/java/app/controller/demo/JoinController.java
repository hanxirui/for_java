package app.controller.demo;

import org.durcframework.core.GridResult;
import org.durcframework.core.controller.SearchController;
import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.subexpression.DefaultJoinExpression;
import org.durcframework.core.expression.subexpression.ValueExpression;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import app.entity.Student;
import app.service.StudentService;

// 连接查询
@Controller
public class JoinController extends SearchController<Student, StudentService> {

	/**
	 * 关联DEPARTMENT表,并且查询外语系的学生
	 * 内连接查询
	 * @return
	 */
	@RequestMapping("/innerJoin_backuser.do")
	public  @ResponseBody GridResult innerJoin(){
		ExpressionQuery query = new ExpressionQuery();
		/*
		 * 关联DEPARTMENT表
		 * department:第二张表的名字
		 * t2:department表的别名
		 * DEPARTMENT:主表的字段
		 * ID:第二张表的字段
		 * 
		 * 这样就会拼接成:inner join department t2 on t.DEPARTMENT = t2.ID
		 */
		// 方式1:
		//query.addJoinExpression(new InnerJoinExpression("department", "t2", "DEPARTMENT", "ID"));
		// 方式2:
		query.addJoinExpression(new DefaultJoinExpression(" inner join department t2 on t.DEPARTMENT = t2.ID"));
		// 查询外语系的学生
		query.add(new ValueExpression("t2.department_name", "外语系"));
		// 返回查询结果
		return this.queryAll(query);
	}
	
	
}
