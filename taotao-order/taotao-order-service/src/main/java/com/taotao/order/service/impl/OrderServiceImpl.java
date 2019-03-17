package com.taotao.order.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.mapper.TbOrderShippingMapper;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbOrderExample;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.taotao.pojo.TbOrder.Status;

/**
 * 订单处理Server
 * <p>Title: OrderServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private TbOrderMapper orderMapper;
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	@Autowired
	private TbOrderShippingMapper orderShippingMapper;
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${ORDER_NO_GEN_KEY}")
	private String ORDER_NO_GEN_KEY;
	@Value("${ORDER_ID_BEGIN_VALUE}")
	private String ORDER_ID_BEGIN_VALUE;
	@Value("${ORDER_ITEM_ID_GEN_KEY}")
	private String ORDER_ITEM_ID_GEN_KEY;

	private String genOrderNo(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String format = sdf.format(new Date());
		//生成订单号,可以使用redis的incr生成,每日重置
		if (!jedisClient.exists(ORDER_NO_GEN_KEY+format.substring(0,8))) {
			//设置初始值
			jedisClient.set(ORDER_NO_GEN_KEY+format.substring(0,8), ORDER_ID_BEGIN_VALUE);
		}
		Long orderSerial = jedisClient.incr(ORDER_NO_GEN_KEY+format.substring(0,8));
		return format + StringUtils.leftPad(orderSerial.toString(),6 ,"0");
	}
	@Override
	public TaotaoResult createOrder(OrderInfo orderInfo) {
		String orderNo = genOrderNo();
		//免邮费
		orderInfo.setPostFee(BigDecimal.ZERO);
		//1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
		orderInfo.setStatus(1);
		//订单创建时间
		orderInfo.setCreateTime(new Date());
		orderInfo.setOrderNo(orderNo);
		orderInfo.setUpdateTime(orderInfo.getUpdateTime());
		//向订单 表插入数据
		orderMapper.insert(orderInfo);
		Long orderId = orderInfo.getId();
		//向订单明细表插入数据。
		List<TbOrderItem> orderItems = orderInfo.getOrderItems();
		for (TbOrderItem tbOrderItem : orderItems) {
			//获得明细主键
			Long oid = jedisClient.incr(ORDER_ITEM_ID_GEN_KEY);
			tbOrderItem.setId(oid);
			tbOrderItem.setOrderId(orderId);
			tbOrderItem.setOrderNo(orderNo);
			//插入明细数据
			orderItemMapper.insert(tbOrderItem);
		}
		//向订单物流表插入数据
		TbOrderShipping orderShipping = orderInfo.getOrderShipping();
		orderShipping.setOrderId(orderId);
		orderShipping.setCreated(new Date());
		orderShipping.setOrderNo(orderNo);
		orderShipping.setUpdated(orderShipping.getCreated());
		orderShippingMapper.insert(orderShipping);
		//返回订单号
		return TaotaoResult.ok(orderId);
	}

	@Override
	public TaotaoResult cancelOrder(String orderNo) {
		TbOrder tbOrder = getByOrderNo(orderNo);
		if (tbOrder == null) {
			return TaotaoResult.fail( String.format("订单号：%s不存在", orderNo));
		}
		tbOrder.setStatus(Status.CANCEL.getCode());
		tbOrder.setCloseTime(new Date());
		TbOrderExample tbOrderExample = new TbOrderExample();
		TbOrderExample.Criteria criteria = tbOrderExample.createCriteria();
		criteria.andStatusEqualTo(Status.ORDERS.getCode());
		criteria.andIdEqualTo(tbOrder.getId());
		int updateNum = orderMapper.updateByExampleSelective(tbOrder, tbOrderExample);
		return TaotaoResult.ok(updateNum);
	}

	@Override
	public TbOrder getByOrderNo(String orderNo) {
		TbOrderExample tbOrderExample = new TbOrderExample();
		TbOrderExample.Criteria criteria = tbOrderExample.createCriteria();
		criteria.andOrderNoEqualTo(orderNo);
		List<TbOrder> tbOrders = orderMapper.selectByExample(tbOrderExample);
		if(CollectionUtils.isEmpty(tbOrders)){
			return null;
		}
		return tbOrders.get(0);
	}

	@Override
	public TaotaoResult payOrder(String orderNo, Integer paymentType) {
		TbOrder tbOrder = getByOrderNo(orderNo);
		if (paymentType == 1||tbOrder.getPaymentType()==1) {
			return TaotaoResult.fail(String.format("订单号：%s是货到付款，无需支付", orderNo));
		}
		if (tbOrder == null) {
			return TaotaoResult.fail( String.format("订单号：%s不存在", orderNo));
		}
		tbOrder.setStatus(Status.PAYING.getCode());
		//余额支付
		if (4 == paymentType) {
			tbOrder.setStatus(Status.PAYED.getCode());
		}
		tbOrder.setPaymentTime(new Date());
		TbOrderExample tbOrderExample = new TbOrderExample();
		TbOrderExample.Criteria criteria = tbOrderExample.createCriteria();
		criteria.andStatusEqualTo(Status.ORDERS.getCode());
		criteria.andIdEqualTo(tbOrder.getId());
		int updateNum = orderMapper.updateByExampleSelective(tbOrder, tbOrderExample);
		return TaotaoResult.ok(updateNum);
	}

}
