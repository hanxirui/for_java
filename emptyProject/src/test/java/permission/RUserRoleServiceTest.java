package permission;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import app.TestBase;
import permission.entity.RUser;
import permission.entity.RUserRole;
import permission.service.RUserRoleService;
import permission.service.RUserService;

public class RUserRoleServiceTest extends TestBase {

	@Autowired
	private RUserRoleService userRoleService;
	@Autowired
	private RUserService userService;
	
	@Test
	public void testGetUserRole(){
		RUser user = userService.getByUsername("sell001");
		List<RUserRole> userRoles = userRoleService.getUserRole(user.getUserId());
		for (RUserRole rUserRole : userRoles) {
			System.out.println(rUserRole.getRoleId());
		}
	}
}
