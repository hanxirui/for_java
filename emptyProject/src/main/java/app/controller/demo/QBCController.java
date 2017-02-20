package app.controller.demo;

import java.util.Arrays;
import java.util.List;

import org.durcframework.core.expression.QBC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import app.dao.StudentDao;
import app.entity.Student;

/**
 * QBC查询
 * @author hc.tang
 *
 */
@Controller
public class QBCController {
	@Autowired
	private StudentDao studentDao;

	@RequestMapping("listqbc_backuser.do")
	public @ResponseBody List<Student> listqbc(){
		// 查询姓名为Jim,并且id是20和25的学生
		// 查询结果以name字段升序
		
		// SELECT * FROM student t WHERE name = 'Jim' AND id IN ( 20,25 ) ORDER BY
		// name ASC
		QBC<Student> qbc = QBC.create(studentDao);
		List<Student> list = qbc
				.eq("name", "Jim")
				.in("id", Arrays.asList(20, 25))
				.sort("name")
				.listAll();
		
		return list;
	}
	
			
	@RequestMapping("listqbc2_backuser.do")
	public  @ResponseBody List<Student> listqbc2(){
		// 查询姓名为Jim,并且id是20和25的学生,并且部门ID为16
		// 查询结果以name字段升序

		// SELECT * FROM student t INNER JOIN department t2 ON
		// t.DEPARTMENT=t2.ID WHERE name = 'Jim' AND t2.ID = 16 AND t.id IN ( 20,25 )
		// ORDER BY name ASC
		QBC<Student> qbc = QBC.create(studentDao);
		List<Student> list = qbc.innerJoin("department", "t2", "DEPARTMENT", "ID")
				.eq("name", "Jim")
				.in("t.id", Arrays.asList(20, 25))
				.eq("t2.ID", 16)
				.sort("name")
				.listAll();
		
		return list;
	}
			
}
