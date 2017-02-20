package permission.service;

import org.durcframework.core.expression.Expression;
import org.durcframework.core.expression.subexpression.ValueExpression;

import permission.entity.RDataPermission;

public class ExpressionFactory {
	private static final byte TYPE_VALUE_EXPRESSION = 1;
	private static final byte TYPE_LIST_EXPRESSION = 2;
	
	public static Expression build(RDataPermission dataPermission){
		if(dataPermission == null){
			return null;
		}
		byte expressionType = dataPermission.getExpressionType();
		String column = dataPermission.getColumn();
		String equal = dataPermission.getEqual();
		String value = dataPermission.getValue();
		
		if(TYPE_VALUE_EXPRESSION == expressionType){
			return new ValueExpression(column, equal, value);
		}
		if(TYPE_LIST_EXPRESSION == expressionType){
			// TODO:....
		}
		
		return null;
	}
	
}
