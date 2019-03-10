package com.taotao.item.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;

public class ItemDelMessageListener implements MessageListener {
    private Logger logger = LoggerFactory.getLogger(ItemDelMessageListener.class);
    @Value("${HTML_OUT_PATH}")
    private String HTML_OUT_PATH;
    private String FILE_SUFFIX=".html";
    @Override
    public void onMessage(Message message) {
        try {
            //从消息中取商品id
            TextMessage textMessage = (TextMessage) message;
            String strId = textMessage.getText();
            Long itemId = Long.parseLong(strId);
            String fileName = itemId + FILE_SUFFIX;
            File htmlFile = new File(HTML_OUT_PATH+fileName);
            if (htmlFile.exists()) {
                htmlFile.delete();
            }
        } catch (Exception e) {
            logger.error("e:{}",e);
        }

    }
}
