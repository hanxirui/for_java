package permission.entity;

public class RGroupUser {
	private int groupId;
	private int userId;
	private String username;

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getGroupId() {
		return this.groupId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	

}