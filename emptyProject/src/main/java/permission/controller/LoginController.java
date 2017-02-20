package permission.controller;

import java.util.Date;

import org.durcframework.core.MessageResult;
import org.durcframework.core.UserContext;
import org.durcframework.core.ValidateHolder;
import org.durcframework.core.controller.BaseController;
import org.durcframework.core.util.ValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import permission.common.RMSContext;
import permission.constant.UserState;
import permission.entity.RUser;
import permission.service.RUserService;
import permission.util.PasswordUtil;

@Controller
public class LoginController extends BaseController{

	@Autowired
	private RUserService rUserService;

	/**
	 * 用户登陆
	 * 
	 * @return
	 */
	@RequestMapping("login.do")
	public @ResponseBody MessageResult login(RUser backUser) {
		
		ValidateHolder validateHolder = ValidateUtil.validate(backUser);
		
		if(validateHolder.isSuccess()){
			if (StringUtils.hasText(backUser.getUsername())) {
				RUser user = rUserService.getByUsername(backUser.getUsername());
				if (user == null || !user.getUsername().equals(backUser.getUsername())) {
					return this.error("用户名密码不正确");
				}
				
				String password = backUser.getPassword();
				String correctHash = user.getPassword();
				
				boolean isPswdCorrect = PasswordUtil.validatePassword(password,
						correctHash);
				
				if (isPswdCorrect) {
					if(user.getState() == UserState.CLOSE) {
						return this.error("无法登录,请联系管理员");
					}
					doLogin(user);
					return this.success();
				}
			}
		}


		return this.error("用户名密码不正确");
	}
	
	private void doLogin(RUser user) {
		// 缓存当前用户角色权限
		RMSContext.getInstance().refreshUserRightData(user.getUserId());
		
		user.setLastLoginDate(new Date());
		UserContext.getInstance().setUser(user);
		rUserService.update(user);
	}

	/**
	 * 注销
	 * 
	 * @return
	 */
	@RequestMapping("logout.do")
	public @ResponseBody MessageResult logout() {
		RMSContext.getInstance().clearCurrentUserRightData();
		UserContext.getInstance().setUser(null);
		return this.success();
	}

}
