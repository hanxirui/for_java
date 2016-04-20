package app.controller;

import org.durcframework.core.controller.CrudController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import app.entity.BuiOrderInfoSch;
import app.entity.OrderInfo;
import app.service.OrderInfoService;

@Controller
public class OrderInfoBuiController extends CrudController<OrderInfo, OrderInfoService> {

	@RequestMapping("/addBuiOrderInfo.do")
	public ModelAndView addOrderInfo(OrderInfo entity) {
		return this.add(entity);
	}

	@RequestMapping("/listBuiOrderInfo.do")
	public ModelAndView listOrderInfo(BuiOrderInfoSch searchEntity) {
		return this.list(searchEntity);
	}

	@RequestMapping("/updateBuiOrderInfo.do")
	public ModelAndView updateOrderInfo(OrderInfo entity) {
		return this.modify(entity);
	}

	@RequestMapping("/delBuiOrderInfo.do")
	public ModelAndView delOrderInfo(OrderInfo entity) {
		return this.remove(entity);
	}

}
