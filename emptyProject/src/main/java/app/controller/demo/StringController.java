package app.controller.demo;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class StringController {
	
	@RequestMapping(value="listString_backuser.do")
	public @ResponseBody String listString(HttpServletResponse response) throws IOException {
		return "hello,世界";
	}
	
}
