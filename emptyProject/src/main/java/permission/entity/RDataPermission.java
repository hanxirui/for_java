package permission.entity;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import permission.constant.ExpressionTypeConst;

public class RDataPermission {

	public static Map<String, String> EXPRESSION_MAP = new TreeMap<String, String>();
	
	static {
		EXPRESSION_MAP.put("=", "等于(=)");
		EXPRESSION_MAP.put(">", "大于(>)");
		EXPRESSION_MAP.put(">=", "大于等于(≥)");
		EXPRESSION_MAP.put("<", "小于(<)");
		EXPRESSION_MAP.put("<=", "小于等于(≤)");
		EXPRESSION_MAP.put("<>", "不等于(≠)");
	}

	private int dpId;
	private int srId;
	private byte expressionType;
	private String column;
	private String equal;
	private String value;
	private String remark;

	private List<Integer> roleId;

	public int getDpId() {
		return dpId;
	}

	public void setDpId(int dpId) {
		this.dpId = dpId;
	}

	public int getSrId() {
		return srId;
	}

	public void setSrId(int srId) {
		this.srId = srId;
	}

	public void setExpressionType(byte expressionType) {
		this.expressionType = expressionType;
	}

	public byte getExpressionType() {
		return this.expressionType;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getColumn() {
		return this.column;
	}

	public void setEqual(String equal) {
		this.equal = equal;
	}

	public String getEqual() {
		return this.equal;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<Integer> getRoleId() {
		return roleId;
	}

	public void setRoleId(List<Integer> roleId) {
		this.roleId = roleId;
	}

	public String getExpression() {
		return this.column + EXPRESSION_MAP.get(equal) + this.value;
	}

	public String getExpressionTypeName() {
		if (this.expressionType == ExpressionTypeConst.ValueExpression
				.getValue()) {
			return "单值查询";
		}

		return "";
	}

}