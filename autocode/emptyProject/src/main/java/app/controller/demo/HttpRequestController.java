package app.controller.demo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.durcframework.core.WebContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HttpRequestController {

	@Autowired
	private HttpServletRequest request;
	
	@RequestMapping("testRequest.do")
	public  String test(HttpServletRequest request2,HttpServletResponse response){
		
		System.out.println(request.getAttribute("a"));
		StringBuilder sb = new StringBuilder();
		sb.append("@Autowired request:"+request).append("<br>")
		.append("test(HttpServletRequest request2):"+request2).append("<br>")
		.append("WebContext request:"+WebContext.getInstance().getRequest()).append("<br>")
		.append(request == WebContext.getInstance().getRequest()).append("<br>")
		.append(request.getSession() == WebContext.getInstance().getRequest().getSession()).append("<br>")
		.append(request.getParameter("name")).append("<br>")
		.append(WebContext.getInstance().getRequest().getParameter("name")).append("<br>")
;		
		
		
		return sb.toString();
	}
	
}
