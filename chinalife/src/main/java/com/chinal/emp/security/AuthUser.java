package com.chinal.emp.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.chinal.emp.entity.Employee;

public class AuthUser extends User {

	/**
	 * <code>serialVersionUID</code> - {description}.
	 */
	private static final long serialVersionUID = 7499093762725434808L;
	private int id;
	private String code;
	private String cName;
	private int level;

	private Employee employee;

	public AuthUser(final String username, final String password, final boolean enabled, final boolean codeNonExpired,
			final boolean credentialsNonExpired, final boolean codeNonLocked,
			final Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, codeNonExpired, credentialsNonExpired, codeNonLocked, authorities);
	}

	/**
	 * @return id - {return content description}
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            - {parameter description}.
	 */
	public void setId(final int id) {
		this.id = id;
	}

	/**
	 * @return code - {return content description}
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            - {parameter description}.
	 */
	public void setCode(final String code) {
		this.code = code;
	}

	public String getcName() {
		return cName;
	}

	public void setcName(String cName) {
		this.cName = cName;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

}
