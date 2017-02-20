package permission.entity;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RSysFunction {
	private int sfId;
	
	@Min(value=1,message="资源ID不能为空")
	private int srId;
	
	private String resName;
	
	@Pattern(regexp="\\w{1,20}",message="权限代码必须为英文或数字或下划线且长度1~20")
	private String operateCode;
	
	@Size(min=1,max=20,message="权限名称长度为1~20")
	private String operateName;
	
	private String url;
	
	public void setOperateName(String operateName) {
		this.operateName = operateName;
	}

	public String getOperateName() {
		return operateName;
	}



	public void setSfId(int sfId) {
		this.sfId = sfId;
	}

	public int getSfId() {
		return this.sfId;
	}

	public String getOperateCode() {
		return operateCode;
	}

	public void setOperateCode(String operateCode) {
		this.operateCode = operateCode;
	}

	public void setSrId(int srId) {
		this.srId = srId;
	}

	public int getSrId() {
		return this.srId;
	}

	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}