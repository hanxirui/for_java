package app.controller;

import org.durcframework.core.support.BsgridController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import app.entity.BsgridOrderInfoSch;
import app.entity.OrderInfo;
import app.service.OrderInfoService;

@Controller
public class OrderInfoBsgridController extends
		BsgridController<OrderInfo, OrderInfoService> {

	@RequestMapping("/addBsgridOrderInfo.do")
	public ModelAndView addOrderInfo(OrderInfo entity) {
		return this.add(entity);
	}

	@RequestMapping("/listBsgridOrderInfo.do")
	public ModelAndView listOrderInfo(BsgridOrderInfoSch searchEntity) {
		return this.list(searchEntity);
	}

	@RequestMapping("/updateBsgridOrderInfo.do")
	public ModelAndView updateOrderInfo(OrderInfo entity) {
		return this.modify(entity);
	}

	@RequestMapping("/delBsgridOrderInfo.do")
	public ModelAndView delOrderInfo(OrderInfo entity) {
		return this.remove(entity);
	}

}
