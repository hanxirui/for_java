package permission.entity;

import javax.validation.constraints.Min;

public class RRolePermission {
	@Min(1)
	private int roleId;
	@Min(1)
	private int sfId;

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getRoleId() {
		return this.roleId;
	}

	public void setSfId(int sfId) {
		this.sfId = sfId;
	}

	public int getSfId() {
		return this.sfId;
	}

}