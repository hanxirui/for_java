package app.controller.demo;

import java.util.List;

import org.durcframework.core.controller.BaseController;
import org.durcframework.core.form.FormModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import app.entity.Department;
import app.entity.Student;

// 请求参数绑定
@Controller
public class DataBindController extends BaseController {

	// ${ctx}/bindList.do?nums[0]=1&nums[1]=2
	@RequestMapping("bindList.do")
	public ModelAndView bindList(@FormModel("nums") List<Integer> nums) {
		for (Integer i : nums) {
			System.out.println(i);
		}
		return this.successView();
	}

	// ${ctx}/bindListStu.do?stus[0].mobile=13712345678&stus[0].name=jim&stus[1].mobile=13987654321&stus[1].name=tom
	@RequestMapping("bindListStu.do")
	public ModelAndView bindListStu(@FormModel("stus") List<Student> stus) {
		for (Student stu : stus) {
			System.out.println(stu.getMobile() + "  " + stu.getName());
		}
		return this.successView();
	}

	// ${ctx}/bindObj.do?stu.id=1&dept.id=2&stu.name=jim
	@RequestMapping("bindObj.do")
	public ModelAndView bindLibindObjstStu(@FormModel("stu") Student student,
			@FormModel("dept") Department dept) {
		/**
		 * 输出: stu.id=1 stu.name=jim dept.id=2
		 */
		System.out.println("stu.id=" + student.getId() + " stu.name="
				+ student.getName());
		System.out.println("dept.id=" + dept.getId());
		return this.successView();
	}

}
