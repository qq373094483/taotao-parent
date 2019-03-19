package com.taotao.ordertimer.service.impl;

import com.taotao.ordertimer.component.ZookeeperComponent;
import com.taotao.ordertimer.service.OrderCancelService;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class OrderCancelServiceImpl implements OrderCancelService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderCancelServiceImpl.class);
    @Value("${SERVER.PORT}")
    private Integer serverPort;

    @Autowired
    private ZookeeperComponent zookeeperComponent;

    @PostConstruct
    public void registZookeeper() {
        try {
            zookeeperComponent.createEphemeralNode("/delayQueue/orderCancel/server/" + serverPort, serverPort + "");
            zookeeperComponent.getData("/delayQueue/orderCancel/server/" + serverPort, event -> {
                LOGGER.info("state:{},path:{},type:{}", event.getState(), event.getPath(), event.getType());
                if (event.getType()==Watcher.Event.EventType.NodeDeleted) {
                    System.out.println("a");
                }
            }, null);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
