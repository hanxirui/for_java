package app.controller.demo;

import java.util.Arrays;

import org.durcframework.core.controller.CrudController;
import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.SqlContent;
import org.durcframework.core.expression.subexpression.LikeRightExpression;
import org.durcframework.core.expression.subexpression.ListExpression;
import org.durcframework.core.expression.subexpression.ValueExpression;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import app.entity.Student;
import app.service.StudentService;


// 不用注解查询,用代码组装查询条件
@Controller
public class StudentController extends CrudController<Student, StudentService> {

	// ${ctx}/listValueExpression.do
	// 查询性别=1的学生
	@RequestMapping("/listValueExpression.do")
	public  ModelAndView list() {
		
		ExpressionQuery query = new ExpressionQuery();
		query.add(new ValueExpression("gender",1));
		return this.list(query);
	}
	
	// ${ctx}/listValueExpression2.do
	// 查询性别为1,并且手机号有133的学生
	@RequestMapping("/listValueExpression2.do")
	public  ModelAndView list2() {
		ExpressionQuery query = new ExpressionQuery();
		query.add(new ValueExpression("gender", "=",1));
		query.add(new LikeRightExpression("mobile", "133"));
		return this.list(query);
	}
	
	// ${ctx}/listValueExpression3.do
	// 查询性别为1,或者性别为0的学生,即查询全部
	@RequestMapping("/listValueExpression3.do")
	public  ModelAndView list3() {
		ExpressionQuery query = new ExpressionQuery();
		query.add(new ValueExpression("gender", "=",1));
		query.add(new ValueExpression(SqlContent.OR, "gender", "=", 0));
		return this.list(query);
	}
	
	// ${ctx}/listListExpression.do
	// 查询主键ID为31,37的学生,即:id in(31,37)
	@RequestMapping("/listListExpression.do")
	public  ModelAndView list4() {
		ExpressionQuery query = new ExpressionQuery();
		query.add(new ListExpression("id", Arrays.asList(31,37)));
		return this.list(query);
	}
	
	// ${ctx}/listOtherExpression.do
	// 如果有其它值需要传入到mybatis中去,则调用
	// query.addParam("mobile", "13398761567");
	@RequestMapping("/listOtherExpression.do")
	public  ModelAndView listOther() {
		ExpressionQuery query = new ExpressionQuery();
		query.add(new ListExpression("id", Arrays.asList(31,37)));
		query.addParam("mobile", "13398761567");
		return this.list(query);
	}
	
	// 通过主键查询某条记录
	// ${ctx}/getById.do
	@RequestMapping("/getById.do")
	public  Student getById(){
		Student schStu = new Student();
		schStu.setId(31);
		
		//Student stu = this.getService().get(schStu); // 这样也可以
		Student stu = this.getService().get(31);
		
		return stu;
	}
}
