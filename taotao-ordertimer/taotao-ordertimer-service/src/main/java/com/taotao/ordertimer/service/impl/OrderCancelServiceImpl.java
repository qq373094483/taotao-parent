package com.taotao.ordertimer.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.ordertimer.component.OrderCancelServerRigster;
import com.taotao.ordertimer.component.ZookeeperComponent;
import com.taotao.ordertimer.order.cancel.OrderCancelDaemonThread;
import com.taotao.ordertimer.service.OrderCancelService;
import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbOrderExample;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderCancelServiceImpl implements OrderCancelService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderCancelServiceImpl.class);
    @Value("${SERVER.PORT}")
    private Integer serverPort;
    @Value("${HOSTNAME}")
    private String HOSTNAME;
    @Autowired
    private OrderCancelDaemonThread orderCancelDaemonThread;
    @Autowired
    private TbOrderMapper tbOrderMapper;
    private String orderCancelRegister;
    private String processing;

    @Autowired
    private ZookeeperComponent zookeeperComponent;
    private List<String> serverList = new ArrayList<>();

    @PostConstruct
    public void registZookeeper() {
        try {
            orderCancelRegister = OrderCancelServerRigster.orderCancelServerListPath + "/" + HOSTNAME;
            processing = "/delayQueue/orderCancel/server/" + HOSTNAME + "/processing";
            zookeeperComponent.createEphemeralNode(orderCancelRegister, null);
            zookeeperComponent.createPersistentNode(processing, null);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void execute(Long orderId) {
        String orderIdPath = processing + "/" + orderId;
        try {
            zookeeperComponent.createPersistentNode(orderIdPath, null);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TbOrderExample tbOrderExample = new TbOrderExample();
        TbOrderExample.Criteria criteria = tbOrderExample.createCriteria();
        criteria.andIdEqualTo(orderId);
        criteria.andStatusEqualTo(TbOrder.Status.ORDERS.getCode());
        criteria.andPaymentTypeGreaterThan(TbOrder.PaymentType.CASH_ON_DELIVERY.getCode());
        List<TbOrder> tbOrders = tbOrderMapper.selectByExample(tbOrderExample);
        if (CollectionUtils.isNotEmpty(tbOrders)) {
            TbOrder tbOrder = tbOrders.get(0);
            //加入等待队列

            System.out.println(tbOrder.getId());
            orderCancelDaemonThread.put(tbOrderMapper,tbOrder);

            return;
        }
        try {
            zookeeperComponent.deleteNode(orderIdPath);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }
}
