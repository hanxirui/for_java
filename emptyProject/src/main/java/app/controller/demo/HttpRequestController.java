package app.controller.demo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.durcframework.core.WebContext;
import org.durcframework.core.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 获取request
 * 
 * @author thc
 *
 */
@Controller
public class HttpRequestController extends BaseController {

	@Autowired
	private HttpServletRequest req1; // 方式1

	@RequestMapping("testRequest_backuser.do")
	public @ResponseBody String test(HttpServletRequest req2 // 方式2
			, HttpServletResponse response) {
		// 方式3
		HttpServletRequest req3 = WebContext.getInstance().getRequest();
		// 方式4
		HttpServletRequest req4 = this.getRequest();
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("private HttpServletRequest req1; // 方式1");
		sb.append("<br>");
		sb.append(req1.getSession());
		
		sb.append("<br>");
		sb.append("(HttpServletRequest req2 // 方式2)");
		sb.append("<br>");
		sb.append(req2.getSession());
		
		sb.append("<br>");
		sb.append("WebContext.getInstance().getRequest()");
		sb.append("<br>");
		sb.append(req3.getSession());
		
		sb.append("<br>");
		sb.append("this.getRequest()");
		sb.append("<br>");
		sb.append(req4.getSession());

		return sb.toString();
	}

}
