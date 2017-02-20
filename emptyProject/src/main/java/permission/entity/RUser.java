package permission.entity;

import java.util.Date;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.durcframework.core.IUser;

import permission.constant.UserState;

public class RUser implements IUser {

	private int userId;
	@Pattern(regexp = "\\w+", message = "用户名只能由数字,字母,下划线组成")
	@Size(min = 4, max = 20, message = "用户名长度范围在4-20之间")
	private String username;
	@Size(min = 6, max = 200, message = "密码长度范围在6-200之间")
	private String password;
	// 状态:1启用,0禁用
	private byte state = UserState.OPEN;
	private Date addTime = new Date();
	private Date lastLoginDate;
	
	@Override
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Date getAddTime() {
		return this.addTime;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public Date getLastLoginDate() {
		return this.lastLoginDate;
	}

	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}

}