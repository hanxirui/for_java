package app.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.durcframework.core.controller.ExportController;
import org.durcframework.core.expression.ExpressionQuery;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import app.entity.OrderInfo;
import app.entity.OrderInfoSch;
import app.service.OrderInfoService;

/**
 * 负责导出
 */
@Controller
public class OrderInfoExportController extends
		ExportController<OrderInfo, OrderInfoService> {

	// 模板路径
	@Override
	public String getTemplateFilePath() {
		HttpServletRequest request = this.getRequest();
		String path = request.getSession().getServletContext().getRealPath("/");
		// D:/.../main/webapp/webapp/orderInfo_export.xls
		// 全路径
		return path +  "/orderInfo_export.xls";
	}
	
	// 导出的文件名
	@Override
	public String getExportFileName() {
		return "订单列表" + System.currentTimeMillis() + ".xls";
	}
	
	@RequestMapping("exportOrderInfo.do")
	public void export(OrderInfoSch orderInfoSch ,HttpServletResponse response){
		ExpressionQuery query = this.buildExpressionQuery(orderInfoSch);
		query.setQueryAll(true);
		this.exportByQuery(query, response);
	}
	

}
