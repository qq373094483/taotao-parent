package com.taotao.order.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("order/option")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping(value="cancel", method=RequestMethod.GET)
    @ResponseBody
    public TaotaoResult cancelOrder(@PathVariable String orderNo) {
        return orderService.cancelOrder(orderNo);
    }

    @RequestMapping(value="pay", method=RequestMethod.GET)
    @ResponseBody
    public TaotaoResult payOrder(@PathVariable String orderNo,Integer paymentType) {
        return orderService.payOrder(orderNo,paymentType);
    }
}
