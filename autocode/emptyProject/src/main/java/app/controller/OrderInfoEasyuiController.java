package app.controller;

import org.durcframework.core.controller.CrudController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import app.entity.OrderInfo;
import app.entity.OrderInfoSch;
import app.service.OrderInfoService;

@Controller
public class OrderInfoEasyuiController extends
		CrudController<OrderInfo, OrderInfoService> {

	@RequestMapping("/addEasyuiOrderInfo.do")
	public ModelAndView addOrderInfo(OrderInfo entity) {
		return this.add(entity);
	}

	@RequestMapping("/listEasyuiOrderInfo.do")
	public ModelAndView listOrderInfo(OrderInfoSch searchEntity) {
		return this.list(searchEntity);
	}

	@RequestMapping("/updateEasyuiOrderInfo.do")
	public ModelAndView updateOrderInfo(OrderInfo entity) {
		return this.modify(entity);
	}

	@RequestMapping("/delEasyuiOrderInfo.do")
	public ModelAndView delOrderInfo(OrderInfo entity) {
		return this.remove(entity);
	}

}