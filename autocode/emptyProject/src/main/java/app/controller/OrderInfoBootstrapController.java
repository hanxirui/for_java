package app.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.durcframework.core.DefaultMessageResult;
import org.durcframework.core.GridResult;
import org.durcframework.core.controller.CrudController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import app.entity.OrderInfo;
import app.entity.OrderInfoSch;
import app.service.OrderInfoService;

@Controller
@RequestMapping("jsp")
public class OrderInfoBootstrapController extends
		CrudController<OrderInfo, OrderInfoService> {
	@RequestMapping("orderInfoManager.do")
	public ModelAndView studentManager(OrderInfoSch orderInfoSch){
		
		GridResult resultHolder = this.query(orderInfoSch);
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 把查询参数带到前台页面
		map.put("searchData", orderInfoSch);
		// 把查询的结果传到页面,通过resultHolder.xx来访问,如resultHolder.list
		map.put("resultHolder", resultHolder);
		
		return new ModelAndView("orderInfoList",map); // WBE-INF/jsp/orderInfoList.jsp
	}
	
	@RequestMapping("orderInfoAdd.do")
	public ModelAndView orderInfoAdd() {
		return new ModelAndView("orderInfoAdd");
	}
	
	@RequestMapping("addOrderInfo.do")
	public String addOrderInfo(OrderInfo orderInfo,ModelMap map) {
		orderInfo.setCreateDate(new Date());
		DefaultMessageResult message = (DefaultMessageResult)this.save(orderInfo);
		if(message.getSuccess()){
			return "forward:orderInfoManager.do";
		}else{
			map.put("orderInfo", orderInfo);
			map.put("error", message);
			return "orderInfoAdd";
		}
	}
	
	@RequestMapping("editOrderInfo.do")
	public ModelAndView editStudent(int id) {
		OrderInfo orderInfo = this.getService().get(id);
		return new ModelAndView("orderInfoUpdate","orderInfo",orderInfo);
	}
	
	@RequestMapping("updateOrderInfo.do")
	public String saveStudent(OrderInfo orderInfo,ModelMap map) {
		DefaultMessageResult message = (DefaultMessageResult)this.update(orderInfo);
		if(message.getSuccess()){
			return "forward:orderInfoManager.do";
		}else{
			map.put("orderInfo", orderInfo);
			map.put("error", message);
			return "orderInfoUpdate";
		}
	}
	
	@RequestMapping("delOrderInfo.do")
	public String delStu(int id) {
		OrderInfo order = new OrderInfo();
		order.setOrderId(id);
		this.getService().del(order);
		return "forward:orderInfoManager.do";
	}
}
