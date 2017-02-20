package permission;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import app.TestBase;
import permission.entity.RRole;
import permission.service.RRoleService;

public class RRoleServiceTest extends TestBase {

	@Autowired
	private RRoleService roleService;
	
	@Test
	public void testGetUserRole(){
		List<RRole> list = roleService.getUserRole(1);
		for (RRole rRole : list) {
			System.out.println(rRole.getRoleName());
		}
	}
	
}
