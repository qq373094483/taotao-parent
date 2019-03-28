package com.taotao.ordertimer.listerner;

import com.taotao.mapper.TbOrderMapper;
import com.taotao.ordertimer.component.ZookeeperComponent;
import com.taotao.ordertimer.order.cancel.OrderCancelDaemonThread;
import com.taotao.pojo.TbOrder;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@Component
public class OrderCreateMessageListener implements MessageListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderCreateMessageListener.class);
    @Autowired
    private ZookeeperComponent zookeeperComponent;
    @Autowired
    private TbOrderMapper tbOrderMapper;
    @Value("${SERVER.PORT}")
    private Integer serverPort;
    @Value("${HOSTNAME}")
    private String HOSTNAME;
    private String executorServer;
    private String processing;
    @Autowired
    private OrderCancelDaemonThread orderCancelDaemonThread;
    @PostConstruct
    public void init() {
        //待执行任务列表
        try {
            executorServer = "/delayQueue/orderCancel/server/list/"+HOSTNAME+":"+serverPort;
            processing="/delayQueue/orderCancel/server/processing";
            zookeeperComponent.createEphemeralNode(executorServer, null);
            zookeeperComponent.createPersistentNode(processing, null);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    //生产者先把数据放入redis,再从消费者处理完业务后，删除掉redis中的数据
    @Override
    public void onMessage(Message message) {
        try {
            //从消息中取商品id
            TextMessage textMessage = (TextMessage) message;
            String strId = textMessage.getText();
            Long orderId = Long.parseLong(strId);
            TbOrder tbOrder = tbOrderMapper.selectByPrimaryKey(orderId);
            orderCancelDaemonThread.put(tbOrderMapper,tbOrder);
            zookeeperComponent.createPersistentNode(processing +"/"+orderId, null);
        } catch (Exception e) {
            LOGGER.error("e:{}", e);
        }

    }
}
