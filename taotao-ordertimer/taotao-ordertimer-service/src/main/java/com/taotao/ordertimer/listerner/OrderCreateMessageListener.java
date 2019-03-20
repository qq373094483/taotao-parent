package com.taotao.ordertimer.listerner;

import com.taotao.mapper.TbOrderMapper;
import com.taotao.pojo.TbOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class OrderCreateMessageListener implements MessageListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderCreateMessageListener.class);
    @Autowired
    private TbOrderMapper tbOrderMapper;
    @Override
    public void onMessage(Message message) {
        try {
            //从消息中取商品id
            TextMessage textMessage = (TextMessage) message;
            String strId = textMessage.getText();
            Long orderId = Long.parseLong(strId);
            TbOrder tbOrder = tbOrderMapper.selectByPrimaryKey(orderId);
            System.out.println(tbOrder.getId());
        } catch (Exception e) {
            LOGGER.error("e:{}",e);
        }

    }
}
