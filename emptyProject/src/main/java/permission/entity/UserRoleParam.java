package permission.entity;

import java.util.List;

import permission.constant.RoleType;

public class UserRoleParam {
	private int userId;
	private List<Integer> roleIds;
	private byte roleType = RoleType.PERSON;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public List<Integer> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<Integer> roleIds) {
		this.roleIds = roleIds;
	}

	public byte getRoleType() {
		return roleType;
	}

	public void setRoleType(byte roleType) {
		this.roleType = roleType;
	}

}
