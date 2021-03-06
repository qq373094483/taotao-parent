package com.taotao.order.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 订单确认页面处理Controller
 * <p>Title: OrderCartController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Controller
@RequestMapping("order")
public class OrderCartController {
	
	@Value("${CART_KEY}")
	private String CART_KEY;
	@Autowired
	private OrderService orderService;

	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource(name = "orderCreateTopic")
	private Destination orderCreateTopicDestination;

	/**
	 * 展示订单确认页面
	 * <p>Title: showOrderCart</p>
	 * <p>Description: </p>
	 * @param request
	 * @return
	 */
	@RequestMapping("order-cart")
	public String showOrderCart(HttpServletRequest request) {
		//用户必须是登录状态
		//取用户id
		TbUser user = (TbUser) request.getAttribute("user");
		System.out.println(user.getUsername());
		//TODO 根据用户信息取收货地址列表，使用静态数据。
		//把收货地址列表取出传递给页面
		//从cookie中取购物车商品列表展示到页面
		List<TbItem> cartList = getCartItemList(request);
		request.setAttribute("cartList", cartList);
		//返回逻辑视图
		return "order-cart";
	}
	
	private List<TbItem> getCartItemList(HttpServletRequest request) {
		//从cookie中取购物车商品列表
		String json = CookieUtils.getCookieValue(request, CART_KEY, true);
		if (StringUtils.isBlank(json)) {
			//如果没有内容，返回一个空的列表
			return new ArrayList<>();
		}
		List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
		return list;
	}
	
	/**
	 * 生成订单处理
	 */
	@RequestMapping(value="create", method=RequestMethod.POST)
	public String createOrder(OrderInfo orderInfo, Model model, HttpServletRequest request, HttpServletResponse response) {
		//生成订单
		TaotaoResult<Long> result = orderService.createOrder(orderInfo);
		//返回逻辑视图
		model.addAttribute("orderId", result.getData());
		model.addAttribute("payment", orderInfo.getPayment());
		//预计送达时间，预计三天后送达
		DateTime dateTime = new DateTime();
		dateTime = dateTime.plusDays(3);
		model.addAttribute("date", dateTime.toString("yyyy-MM-dd"));
		CookieUtils.deleteCookie(request,response,CART_KEY);
		jmsTemplate.send(orderCreateTopicDestination, session -> session.createTextMessage(result.getData() + ""));
		return "success";
	}



}
