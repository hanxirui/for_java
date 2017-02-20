package permission.entity;

import org.durcframework.core.expression.annotation.ValueField;
import org.durcframework.core.support.SearchBUI;

public class RDataPermissionSch extends SearchBUI {

	private Integer dpIdSch;
	private Integer srIdSch;
	private Byte expressionTypeSch;
	private String columnSch;
	private String equalSch;
	private String valueSch;
	private String remarkSch;

	public Integer getDpIdSch() {
		return dpIdSch;
	}

	public void setDpIdSch(Integer dpIdSch) {
		this.dpIdSch = dpIdSch;
	}

	public void setSrIdSch(Integer srIdSch) {
		this.srIdSch = srIdSch;
	}

	@ValueField(column = "sr_id")
	public Integer getSrIdSch() {
		return this.srIdSch;
	}

	public void setExpressionTypeSch(Byte expressionTypeSch) {
		this.expressionTypeSch = expressionTypeSch;
	}

	@ValueField(column = "expression_type")
	public Byte getExpressionTypeSch() {
		return this.expressionTypeSch;
	}

	public void setColumnSch(String columnSch) {
		this.columnSch = columnSch;
	}

	@ValueField(column = "column")
	public String getColumnSch() {
		return this.columnSch;
	}

	public void setEqualSch(String equalSch) {
		this.equalSch = equalSch;
	}

	@ValueField(column = "equal")
	public String getEqualSch() {
		return this.equalSch;
	}

	public void setValueSch(String valueSch) {
		this.valueSch = valueSch;
	}

	@ValueField(column = "value")
	public String getValueSch() {
		return this.valueSch;
	}

	public void setRemarkSch(String remarkSch) {
		this.remarkSch = remarkSch;
	}

	@ValueField(column = "remark")
	public String getRemarkSch() {
		return this.remarkSch;
	}

}