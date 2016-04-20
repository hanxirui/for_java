package app.controller.demo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.durcframework.core.SpringContext;
import org.durcframework.core.WebContext;
import org.durcframework.core.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import app.service.StudentService;

@Controller
public class HttpRequestController2 extends BaseController {
	
	//${ctx}/testRequest2.do?name=123123
	@RequestMapping("testRequest2.do")
	public  String test(HttpServletRequest request2,HttpServletResponse  response){
		
		HttpServletRequest request3 = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		StringBuilder sb = new StringBuilder();
		sb.append("this.getRequest():"+this.getRequest()).append("<br>")
		.append("test(HttpServletRequest request2):"+request2).append("<br>")
		.append("WebContext.getInstance().getRequest():"+WebContext.getInstance().getRequest()).append("<br>")
		.append("((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest():" + request3).append("<br>")
		.append("this.getRequest() == request2:").append(this.getRequest() == request2).append("<br>")
		.append("this.getRequest() == request3:").append(this.getRequest() == request3).append("<br>")
		.append("request2 == request3:").append(request2 == request3).append("<br>")
		.append(this.getRequest().getParameter("name")).append("<br>")
		.append(request2.getParameter("name")).append("<br>")
		.append("SpringContext.getBean(StudentService.class):").append(SpringContext.getBean(StudentService.class))
;		
		
		return sb.toString();
	}
}
