package app;

import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.subexpression.ValueExpression;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import app.entity.Student;
import app.service.StudentService;

class Group {
	
}

public class ReulstCountTest extends TestBase {

	
	@Autowired
	private StudentService studentService;
	
	@Test
	public void testUpdateCount() {
		Student stu = new Student();
		stu.setName("Jim");
		
		ExpressionQuery query = new ExpressionQuery();
		query.add(new ValueExpression("name", "Jim"));
		
		int count = studentService.delByExpression(query);
		
		System.out.println(count);
		
		Assert.isTrue(count > 0);
	}
	
}
