package app.controller.demo;

import java.util.concurrent.Callable;

import org.apache.poi.hssf.record.formula.functions.T;
import org.durcframework.core.GridResult;
import org.durcframework.core.controller.SearchController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.WebAsyncTask;

import app.entity.SearchStudentEntity;
import app.entity.Student;
import app.service.StudentService;

// 异步调用
@Controller
public class AsyncSearchController extends SearchController<Student, StudentService> {

	// 同步
	@RequestMapping("/listSync_backuser.do")
	public @ResponseBody GridResult listSync_backuser(SearchStudentEntity searchStudentEntity) {
		long startTime = System.currentTimeMillis();

		GridResult grid = this.query(searchStudentEntity);

		System.out.println("耗时:" + (System.currentTimeMillis() - startTime));

		return grid;
	}

	// 异步
	@RequestMapping("/listAsync_backuser.do")
	public @ResponseBody WebAsyncTask<GridResult> listAsync_backuser(final SearchStudentEntity searchEntity) {
		long startTime = System.currentTimeMillis();

		System.out.println("/longtimetask被调用 thread id is : " + Thread.currentThread().getId());
		Callable<GridResult> callable = new Callable<GridResult>() {
			public GridResult call() throws Exception {
				System.out.println("执行成功 thread id is : " + Thread.currentThread().getId());
				return query(searchEntity);
			}
		};
		WebAsyncTask<GridResult> task = new WebAsyncTask<GridResult>(callable);

		System.out.println("耗时:" + (System.currentTimeMillis() - startTime));
				
		return task;
	}
	
	
}
