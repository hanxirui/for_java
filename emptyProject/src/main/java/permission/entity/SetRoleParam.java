package permission.entity;

import java.util.List;

public class SetRoleParam {
	private int roleId;
	private String roleName;
	private List<Integer> sfId;

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public List<Integer> getSfId() {
		return sfId;
	}

	public void setSfId(List<Integer> sfId) {
		this.sfId = sfId;
	}

}
