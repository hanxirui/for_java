package app.controller.demo;

import org.durcframework.core.MessageResult;
import org.durcframework.core.controller.CrudController;
import org.durcframework.core.util.DateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import app.entity.SearchStudentEntity;
import app.entity.Student;
import app.service.StudentService;

/**
 * 接收时间参数
 * @author hc.tang
 * 2013年12月10日
 *
 */
@Controller
public class DateFormatController extends CrudController<Student, StudentService> {
	
	/**
	 * 重写父类方法,使得时间参数精确到时分秒
	 * 默认不重写的话默认只精确到天,即yyyy-MM-dd
	 */
	@Override
	protected String getDateFormatPattern() {
		return "yyyy-MM-dd HH:mm:ss";
	}
	
	// ${ctx}/listDateFormat_backuser.do?&schBirthday=2011-12-02%2012:11:00
	@RequestMapping(value="/listDateFormat_backuser.do",method=RequestMethod.GET)
	public @ResponseBody MessageResult listDateFormat(SearchStudentEntity searchStudentEntity) {
		return this.success(DateUtil.ymdhmsFormat(searchStudentEntity.getSchBirthday()));
	}
	
	
	
}
