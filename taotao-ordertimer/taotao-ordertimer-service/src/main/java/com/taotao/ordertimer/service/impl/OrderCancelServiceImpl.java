package com.taotao.ordertimer.service.impl;

import com.taotao.ordertimer.service.OrderCancelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class OrderCancelServiceImpl implements OrderCancelService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderCancelServiceImpl.class);
    @Value("${SERVER.PORT}")
    private Integer serverPort;

    @PostConstruct
    public void registZookeeper() {
        System.out.println(serverPort);
        System.out.println("爱美网");
        LOGGER.info("黄");
        System.out.println(System.getProperty("file.encoding"));
    }
}
