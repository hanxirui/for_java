package permission.entity;

import java.util.List;

public class FunctionRoleParam {
	private int sfId;
	private List<Integer> roleIds;

	public int getSfId() {
		return sfId;
	}

	public void setSfId(int sfId) {
		this.sfId = sfId;
	}

	public List<Integer> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<Integer> roleIds) {
		this.roleIds = roleIds;
	}

}
