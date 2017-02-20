package permission.constant;

public enum ExpressionTypeConst {
	ValueExpression((byte)1)
	,ListExpression((byte)2);
	
	private byte value;
	
	ExpressionTypeConst(byte value){
		this.value = value;
	}
	
	public byte getValue() {
		return value;
	}
	
}
