package app.controller.demo;

import java.util.List;

import org.durcframework.core.GridResult;
import org.durcframework.core.controller.SearchController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import app.entity.SearchStudentEntity;
import app.entity.Student;
import app.service.StudentService;

@Controller
public class PageParamNameController extends SearchController<Student, StudentService> {

	// 重写父类方法,返回自定义的结果类
	// json序列化时将根据自定义的get方法来序列化
	@Override
	protected GridResult getGridResult() {
		return new MyResultHolder();
	}

	/*
	 * {"list":[{...},{...}],"total_count":36,"page_index":1,"page_length":10,
	 * "page_count":3}
	 */
	// ${ctx}/pageParamNameList.do
	@RequestMapping("/pageParamNameList_backuser.do")
	public @ResponseBody GridResult pageParamNameList(SearchStudentEntity searchStudentEntity) {
		return this.query(searchStudentEntity);
	}

	// 自定义的结果类,用来保存查询结果信息
	public class MyResultHolder implements GridResult {
		private static final long serialVersionUID = 1L;

		private List<?> list;

		private int start;
		private int total_count;
		private int page_index;
		private int page_length;
		private int page_count;

		@Override
		public void setList(List<?> list) {
			this.list = list;
		}

		@Override
		public void setTotal(int total) {
			this.total_count = total;
		}

		@Override
		public void setPageIndex(int pageIndex) {
			this.page_index = pageIndex;
		}

		@Override
		public void setPageSize(int pageSize) {
			this.page_length = pageSize;
		}

		@Override
		public void setPageCount(int pageCount) {
			this.page_count = pageCount;
		}

		public List<?> getList() {
			return list;
		}

		public int getTotal_count() {
			return total_count;
		}

		public int getPage_index() {
			return page_index;
		}

		public int getPage_length() {
			return page_length;
		}

		public int getPage_count() {
			return page_count;
		}

		public int getStart() {
			return start;
		}

		@Override
		public void setStart(int start) {
			this.start = start;
		}

		@Override
		public void setSuccess(boolean success) {

		}

		@Override
		public void setMessage(String message) {

		}

		@Override
		public void setMessages(List<String> messages) {

		}

		@Override
		public void setAttach(Object attach) {
			// TODO Auto-generated method stub
			
		}

	}
}
