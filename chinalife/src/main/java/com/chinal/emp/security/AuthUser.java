package com.chinal.emp.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class AuthUser extends User {

	/**
	 * <code>serialVersionUID</code> - {description}.
	 */
	private static final long serialVersionUID = 7499093762725434808L;
	private int id;
	private String account;
	private String cName;

	public AuthUser(final String username, final String password, final boolean enabled,
			final boolean accountNonExpired, final boolean credentialsNonExpired, final boolean accountNonLocked,
			final Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
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
	 * @return account - {return content description}
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * @param account
	 *            - {parameter description}.
	 */
	public void setAccount(final String account) {
		this.account = account;
	}

	public String getcName() {
		return cName;
	}

	public void setcName(String cName) {
		this.cName = cName;
	}

}
