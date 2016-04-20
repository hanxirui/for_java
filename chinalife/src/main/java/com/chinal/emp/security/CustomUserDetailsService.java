package com.chinal.emp.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.subexpression.ValueExpression;
import org.durcframework.core.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.chinal.emp.dao.EmployeeDao;
import com.chinal.emp.entity.Employee;

/**
 * 一个自定义的service用来和数据库进行操作. 即以后我们要通过数据库保存权限.则需要我们继承UserDetailsService
 * 
 * @author liukai
 * 
 */
public class CustomUserDetailsService extends CrudService<Employee, EmployeeDao> implements UserDetailsService {

	protected static Logger logger = Logger.getLogger("service");

	@Autowired
	Md5PasswordEncoder passwordEncoder;

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {

		UserDetails user = null;

		try {
			System.out.println(passwordEncoder.encodePassword("123456", "admin"));
			// 搜索数据库以匹配用户登录名.
			// 我们可以通过dao使用JDBC来访问数据库
			ExpressionQuery query = new ExpressionQuery();
			query.add(new ValueExpression("account", username));

			List<Employee> emps = this.find(query);
			if (emps.size() == 1) {
				Employee dbUser = emps.get(0);
				// Populate the Spring User object with details from the dbUser
				// Here we just pass the username, password, and access level
				// getAuthorities() will translate the access level to the
				// correct
				// role type
				user = new User(dbUser.getName(), dbUser.getPassword().toLowerCase(), true, true, true, true,
						getAuthorities(dbUser.getRole()));
			}

		} catch (Exception e) {
			logger.error("Error in retrieving user");
			throw new UsernameNotFoundException("Error in retrieving user");
		}

		return user;
	}

	/**
	 * 获得访问角色权限
	 * 
	 * @param access
	 * @return
	 */
	public Collection<SimpleGrantedAuthority> getAuthorities(Integer access) {

		List<SimpleGrantedAuthority> authList = new ArrayList<SimpleGrantedAuthority>(5);

		// 所有的用户默认拥有ROLE_USER权限
		authList.add(new SimpleGrantedAuthority("ROLE_USER"));
		authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		if (access.compareTo(1) == 0) {
			authList.add(new SimpleGrantedAuthority("ROLE_1"));
		}
		if (access.compareTo(2) == 0) {
			authList.add(new SimpleGrantedAuthority("ROLE_2"));
			authList.add(new SimpleGrantedAuthority("ROLE_1"));
		}
		if (access.compareTo(3) == 0) {
			authList.add(new SimpleGrantedAuthority("ROLE_3"));
			authList.add(new SimpleGrantedAuthority("ROLE_2"));
			authList.add(new SimpleGrantedAuthority("ROLE_1"));
		}
		if (access.compareTo(4) == 0) {
			authList.add(new SimpleGrantedAuthority("ROLE_4"));
			authList.add(new SimpleGrantedAuthority("ROLE_3"));
			authList.add(new SimpleGrantedAuthority("ROLE_2"));
			authList.add(new SimpleGrantedAuthority("ROLE_1"));
		}
		if (access.compareTo(5) == 0) {
			authList.add(new SimpleGrantedAuthority("ROLE_5"));
			authList.add(new SimpleGrantedAuthority("ROLE_4"));
			authList.add(new SimpleGrantedAuthority("ROLE_3"));
			authList.add(new SimpleGrantedAuthority("ROLE_2"));
			authList.add(new SimpleGrantedAuthority("ROLE_1"));
		}

		return authList;
	}
}
