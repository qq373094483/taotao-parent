package com.taotao.ordertimer.listerner;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.ordertimer.component.ZookeeperComponent;
import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbOrderExample;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.List;

@Component
public class OrderCreateMessageListener implements MessageListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderCreateMessageListener.class);
    @Autowired
    private ZookeeperComponent zookeeperComponent;
    private static final String WAIT_FOR_TASK_PATH = "/delayQueue/orderCancel/server";

    @PostConstruct
    public void init() {
        //待执行任务列表
        try {
            zookeeperComponent.createPersistentNode(WAIT_FOR_TASK_PATH, null);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    private TbOrderMapper tbOrderMapper;

    @Override
    public void onMessage(Message message) {
        try {
            //从消息中取商品id
            TextMessage textMessage = (TextMessage) message;
            String strId = textMessage.getText();
            Long orderId = Long.parseLong(strId);
            TbOrderExample tbOrderExample = new TbOrderExample();
            TbOrderExample.Criteria criteria = tbOrderExample.createCriteria();
            criteria.andIdEqualTo(orderId);
            criteria.andStatusEqualTo(TbOrder.Status.ORDERS.getCode());
            criteria.andPaymentTypeGreaterThan(TbOrder.PaymentType.CASH_ON_DELIVERY.getCode());
            List<TbOrder> tbOrders = tbOrderMapper.selectByExample(tbOrderExample);
            if (CollectionUtils.isNotEmpty(tbOrders)) {
                System.out.println(tbOrders.get(0).getId());
                zookeeperComponent.createPersistentNode(WAIT_FOR_TASK_PATH+"/"+tbOrders.get(0).getId(), null);
            }
        } catch (Exception e) {
            LOGGER.error("e:{}", e);
        }

    }
}
