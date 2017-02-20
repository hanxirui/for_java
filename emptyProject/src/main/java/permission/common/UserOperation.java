package permission.common;

public class UserOperation {

	public UserOperation() {
	}

	public UserOperation(String operateCode, String url) {
		this.operateCode = operateCode;
		this.url = url;
	}

	private String operateCode;
	private String url;

	public String getOperateCode() {
		return operateCode;
	}

	public void setOperateCode(String operateCode) {
		this.operateCode = operateCode;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
