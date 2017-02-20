package permission.common;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.durcframework.core.UserContext;

import permission.entity.RUser;

public class RMSSessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent event) {
	}

	/**
	 * 用户session失效,移除用户权限数据
	 */
	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		RUser user = UserContext.getInstance().getUser(event.getSession());
		if(user != null){
			RMSContext.getInstance().clearUserRightData(user.getUserId());
		}
	}

}
