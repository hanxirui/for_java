package permission.entity;

import permission.constant.RoleType;

public class RRole {
	private int roleId;
	private String roleName;
	private byte roleType = RoleType.PERSON;
	
	public String getRoleTypeName() {
		if(roleType == RoleType.PERSON) {
			return RoleType.PERSON_NAME;
		}else if(roleType == RoleType.GROUP) {
			return RoleType.GROUP_NAME;
		}else{
			return null;
		}
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getRoleId() {
		return this.roleId;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public byte getRoleType() {
		return roleType;
	}

	public void setRoleType(byte roleType) {
		this.roleType = roleType;
	}
	
	

}