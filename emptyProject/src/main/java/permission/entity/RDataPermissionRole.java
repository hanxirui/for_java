package permission.entity;

public class RDataPermissionRole {
	
	public RDataPermissionRole() {
	}

	public RDataPermissionRole(int dpId, int roleId) {
		this.dpId = dpId;
		this.roleId = roleId;
	}

	private int dpId;
	private int roleId;

	public void setDpId(int dpId) {
		this.dpId = dpId;
	}

	public int getDpId() {
		return this.dpId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getRoleId() {
		return this.roleId;
	}

}