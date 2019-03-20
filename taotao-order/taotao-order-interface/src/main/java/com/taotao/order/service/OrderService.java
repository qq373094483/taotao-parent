package com.taotao.order.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.pojo.TbOrder;

public interface OrderService {

	TaotaoResult<Long> createOrder(OrderInfo orderInfo);

	TaotaoResult cancelOrder(String orderNo);

	TbOrder getByOrderNo(String orderNo);

	TaotaoResult payOrder(String orderNo,Integer paymentType);
}
