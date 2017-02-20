package app.controller.demo;

import java.util.Map;

import org.durcframework.core.EntityProcessor;
import org.durcframework.core.GridResult;
import org.durcframework.core.controller.CrudController;
import org.durcframework.core.expression.ExpressionQuery;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import app.entity.Student;
import app.service.StudentService;

// 处理结果集
/*
 * 有些查询出来的数据并不是我们想要在页面上显示的,
 * 需要通过二次处理,比如性别字段,数据库中保存的是0,1.
 * 页面上显示的是女,男
 */
@Controller
public class ProcessorController extends CrudController<Student, StudentService> {

	// ${ctx}/listProcessor.do
	@RequestMapping("/listProcessor_backuser.do")
	public  @ResponseBody GridResult listPage() {
		ExpressionQuery query = new ExpressionQuery();
		// 添加排序
		query.addSort("regist_date","desc");
		
		return this.queryWithProcessor(query, new EntityProcessor<Student>() {
			// jsonObject是当前记录的json对象,可以对其增加键值对
			@Override
			public void process(Student entity, Map<String,Object> jsonObject) {
				// 在返回的json中会增加myGender:'男' 或  myGender:'女'
				if(entity.getGender() == 1) {
					jsonObject.put("myGender", "男");
				}else{
					jsonObject.put("myGender", "女");
				}
			}
			
		});
	}
	
	
}
