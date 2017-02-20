package permission.entity;

public class RGroupRole {
	private int groupId;
	private int roleId;
	private String roleName;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getGroupId() {
		return this.groupId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getRoleId() {
		return this.roleId;
	}

}