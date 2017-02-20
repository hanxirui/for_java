package app;

import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.subexpression.ValueExpression;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import app.entity.OrderInfo;
import app.service.OrderInfoService;

public class OrderInfoDaoTest extends TestBase {
	
	@Autowired
	private OrderInfoService service;
	
	// 保存不为null的字段
	@Test
	public void testSaveNotNull() {
		OrderInfo info = new OrderInfo();
		info.setMobile("13322225555");
		service.saveNotNull(info);
	}
	
	// 根据条件更新
	// 将手机号为13322225555的改成13322226666
	@Test
	public void testUpdateByExpression() {
		OrderInfo info = new OrderInfo();
		info.setMobile("13322226666");
		
		ExpressionQuery query = new ExpressionQuery();
		query.add(new ValueExpression("mobile", "13322225555"));
		
		service.updateByExpression(info, query);
	}
	
	// 更新数据,只更新不为null的
	@Test
	public void testUpdateNotNull() {
		ExpressionQuery query = new ExpressionQuery();
		query.add(new ValueExpression("mobile", "13322226666"));
		OrderInfo info = service.getByExpression(query);
		info.setAddress("aaa");
		service.updateNotNull(info);
	}
	
	// 将手机号为13322226666的改成13322227777
	// 不影响其它字段
	@Test
	public void testUpdateNotNullByExpression() {
		OrderInfo info = new OrderInfo();
		info.setMobile("13322227777");
		
		ExpressionQuery query = new ExpressionQuery();
		query.add(new ValueExpression("mobile", "13322226666"));
		
		service.updateNotNullByExpression(info, query);
	}
	
	// 删除手机号为13322227777的记录
	@Test
	public void testDelByExpression() {
		ExpressionQuery query = new ExpressionQuery();
		query.add(new ValueExpression("mobile", "13322227777"));
		
		service.delByExpression(query);
	}
}
