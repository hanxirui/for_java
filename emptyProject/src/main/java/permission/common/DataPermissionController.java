package permission.common;

import java.util.List;

import org.durcframework.core.SearchSupport;
import org.durcframework.core.controller.CrudController;
import org.durcframework.core.dao.BaseDao;
import org.durcframework.core.expression.Expression;
import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.service.CrudService;

/**
 * 用到数据权限检查的功能,可以继承这个类
 * @author hc.tang
 *
 * @param <Entity>
 * @param <Service>
 */
public class DataPermissionController<Entity, Service extends CrudService<Entity, ? extends BaseDao<Entity>>> extends CrudController<Entity, Service> {

	
	@Override
	protected ExpressionQuery buildExpressionQuery(SearchSupport searchEntity) {
		ExpressionQuery query = super.buildExpressionQuery(searchEntity);
		List<Expression> userDataExpressions = RMSContext.getInstance().getUserDataExpressions();
		query.addAll(userDataExpressions);
		return query;
	}
	
	public ExpressionQuery buildExpressionQuery() {
		ExpressionQuery query = new ExpressionQuery();
		List<Expression> userDataExpressions = RMSContext.getInstance().getUserDataExpressions();
		query.addAll(userDataExpressions);
		return query;
	}
	
}
