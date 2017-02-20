package permission;

import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.subexpression.ValueExpression;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import app.TestBase;
import permission.entity.RUser;
import permission.service.RUserService;

public class RUserServiceTest extends TestBase {
	
	@Autowired
	RUserService rUserService;
	
	@Test
	public void testList(){
//		List<RUser> list = rUserService.find(new ExpressionQuery());
//		Assert.notEmpty(list);
//		
//		RUser user1 = rUserService.get(3);
//		Assert.notNull(user1);
		
		ExpressionQuery query = new ExpressionQuery();
		query.add(new ValueExpression("user_id", ">",2));
		int i = rUserService.delByExpression(query);
		
		System.out.println(i);
		
		Assert.isTrue(i > 0);
		
//		RUser u = new RUser();
//		u.setUsername("admin");
//		RUser user2 = rUserService.get("admin");
//		Assert.notNull(user2);
		
	}

}
