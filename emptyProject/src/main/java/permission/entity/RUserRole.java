package permission.entity;

import permission.constant.RoleType;

public class RUserRole {
	private int userId;
	private int roleId;
	private byte roleType = RoleType.PERSON;
	private String username;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getRoleId() {
		return this.roleId;
	}

	public byte getRoleType() {
		return roleType;
	}

	public void setRoleType(byte roleType) {
		this.roleType = roleType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}