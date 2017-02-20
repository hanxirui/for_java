package permission;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import app.TestBase;
import permission.common.Md5Encrypt;
import permission.entity.RUser;
import permission.service.RUserService;
import permission.util.PasswordUtil;

public class RestPasswordTest extends TestBase {

	@Autowired
	RUserService userService;
	
	// 将所有用户密码初始化为123456
	@Test
	public void testList() {
		String newPswd = "123456";
		newPswd = Md5Encrypt.encrypt(newPswd);
		List<RUser> users = userService.getAllUser();
		for (RUser user : users) {
			userService.updateUserPassword(user, newPswd);
		}
	}
	
	@Test
	public void restGetPswd() {
		String newPswd = "123456";
		newPswd = Md5Encrypt.encrypt(newPswd);
		String ret = PasswordUtil.createHash(newPswd);
		// 1000:8dc4c0d4e2d3585d3d47ed593c96a1bf0430247e09b09ccf:e6a6747ce9296cb6a67c95d278a4ab4cf37aa9a41801cc5f
		System.out.println(ret);
	}
	
}
