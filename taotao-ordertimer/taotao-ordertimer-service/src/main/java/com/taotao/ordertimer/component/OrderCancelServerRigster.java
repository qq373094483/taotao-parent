package com.taotao.ordertimer.component;

import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class OrderCancelServerRigster {
    public static final String orderCancelServerListPath = "/delayQueue/orderCancel/server/list";
    @Autowired
    private ZookeeperComponent zookeeperComponent;
    @PostConstruct
    public void init() {
        try {
            zookeeperComponent.createPersistentNode(orderCancelServerListPath,null);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
