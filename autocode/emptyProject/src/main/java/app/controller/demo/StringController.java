package app.controller.demo;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StringController {
	
	@RequestMapping(value="listString")
	public void listString(HttpServletResponse response) throws IOException {
		response.getWriter().write("hello,世界");
	}
	
}
