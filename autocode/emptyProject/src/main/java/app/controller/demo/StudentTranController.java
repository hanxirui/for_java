package app.controller.demo;

import org.durcframework.core.DurcException;
import org.durcframework.core.controller.CrudController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import app.entity.Student;
import app.service.StudentService;

// 事务处理
@Controller
public class StudentTranController extends CrudController<Student, StudentService>{

	@Autowired
	private TransactionTemplate transactionTemplate;
	
	// ${ctx}/updateTran.do
	@RequestMapping("/updateTran.do")
	public  ModelAndView updateTran() {
		
		transactionTemplate.execute(new TransactionCallback<Student>() {

			@SuppressWarnings("unused")
			@Override
			public Student doInTransaction(TransactionStatus arg0) {
				
				try{
					Student student = getService().get(1);
					student.setName("李四3");
					getService().update(student);
					int i = 1/0; // 模拟出错
				}catch(Exception e) {
					arg0.setRollbackOnly();
					throw new DurcException("修改出错,事务回滚");
				}
				
				return null;
			}
		});
		
		return this.successView();
	}
	
	
}
