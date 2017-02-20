package permission.common;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.durcframework.core.DefaultMessageResult;
import org.durcframework.core.IUser;
import org.durcframework.core.UserContext;
import org.durcframework.core.util.RequestUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;

import permission.util.RightUtil;

/**
 * URL拦截
 * @author Administrator
 *
 */
public class UserUrlInterceptor implements HandlerInterceptor{
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		boolean isValidUrl = isValidUrl(request, response);
		
		if(isValidUrl) {
			return true;
		}
		
		this.fireFailHanlder(request, response);
		
		return false;
	}
	
	private boolean isValidUrl(HttpServletRequest request,HttpServletResponse response) {
		if(UserContext.isExcludeUrl(request)) {
			return true;
		}
		
		String url = RequestUtil.getRequestPath(request);
		url = url.substring(url.lastIndexOf("/") + 1);
		IUser user = UserContext.getInstance().getUser(request.getSession());
		
		return RightUtil.checkCurrentUserUrl(user.getUserId(),url);
	}
	
	protected void fireFailHanlder(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		if (RequestUtil.isAjaxRequest(request)) {
			response.setContentType("text/html;charset=UTF-8");
			String errorJson = JSON.toJSONString(DefaultMessageResult.error("您无权操作"));
			response.getWriter().write(errorJson);
			return;
		}
		
		response.sendRedirect(request.getContextPath() + "/exclude/noPermission_sd.jsp");
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}
	
}
