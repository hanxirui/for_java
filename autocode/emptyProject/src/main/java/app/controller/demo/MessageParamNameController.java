package app.controller.demo;

import java.util.Arrays;
import java.util.List;

import org.durcframework.core.MessageResult;
import org.durcframework.core.controller.SearchController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import app.entity.SearchStudentEntity;
import app.entity.Student;
import app.service.StudentService;

@Controller
public class MessageParamNameController extends
		SearchController<Student, StudentService> {
	
	// 重写消息类,json将根据返回的类来序列化
	// 默认返回org.durcframework.core.DefaultMessageResultew对象
	@Override
	protected MessageResult getMessageResult() {
		return new MyMessageHolder();
	}

	@RequestMapping("/messageParamName.do")
	public 
	ModelAndView messageParamName(SearchStudentEntity searchStudentEntity) {
		return this.successView("success");
	}
	
	@RequestMapping("/messageParamName2.do")
	public 
	ModelAndView messageParamName2(SearchStudentEntity searchStudentEntity) {
		return this.errorView("失败!");
	}
	
	@RequestMapping("/messageParamName3.do")
	public 
	ModelAndView messageParamName3(SearchStudentEntity searchStudentEntity) {
		List<String> errorList = Arrays.asList("失败原因1","失败原因2");
		return this.errorView("失败!",errorList);
	}
	
	// 自定义的消息类
	public class MyMessageHolder implements MessageResult {
		private static final long serialVersionUID = 1L;
		
		private boolean req_success;
		private String response_message;
		private String response_error;
		private List<String> other_errors;

		@Override
		public void setSuccess(boolean success) {
			this.req_success = success;
		}

		@Override
		public void setMessage(String message) {
			this.response_message = message;
		}

		@Override
		public void setMessages(List<String> messages) {
			other_errors = messages;
		}

		public boolean isReq_success() {
			return req_success;
		}

		public String getResponse_message() {
			return response_message;
		}

		public String getResponse_error() {
			return response_error;
		}

		public List<String> getOther_errors() {
			return other_errors;
		}

	}
}
