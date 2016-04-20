package app.controller;

import org.durcframework.core.controller.CrudController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import app.entity.SearchStudentEntity;
import app.entity.Student;
import app.service.StudentService;

@Controller
public class StudentCrudController extends
		CrudController<Student, StudentService> {

	@RequestMapping("/addStudent.do")
	public ModelAndView addStudent(Student student) {
		return this.add(student);
	}

	@RequestMapping("/listStudent.do")
	public ModelAndView listStudent(SearchStudentEntity searchStudentEntity) {
		return this.list(searchStudentEntity);
	}

	@RequestMapping("/updateStudent.do")
	public ModelAndView updateStudent(Student student) {
		return this.modify(student);
	}

	// 传一个id值即可,根据主键删除
	@RequestMapping("/delStudent.do")
	public ModelAndView delStudent(Student student) {
		return this.remove(student);
	}

}
