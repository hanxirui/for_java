package permission.entity;

import java.util.List;

public class RGroup {
	private int groupId;
	private String groupName;
	private int parentId;

	private List<RGroup> children;
	private List<RRole> roles;

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getGroupId() {
		return this.groupId;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public int getParentId() {
		return this.parentId;
	}

	public List<RGroup> getChildren() {
		return children;
	}

	public void setChildren(List<RGroup> children) {
		this.children = children;
	}

	public List<RRole> getRoles() {
		return roles;
	}

	public void setRoles(List<RRole> roles) {
		this.roles = roles;
	}

}