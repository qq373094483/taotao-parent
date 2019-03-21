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
import java.util.List;

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
            String orderCancel = "/delayQueue/orderCancel";
            String delayQueueServer = orderCancel + "/server/";
            String serverFlag = "server:" + serverPort;
            String waitForMission = delayQueueServer + serverFlag + "/waitForMission";
            String processing = delayQueueServer + serverFlag + "/processing";

            zookeeperComponent.createEphemeralNode(delayQueueServer + serverPort, null);
            zookeeperComponent.createPersistentNode(waitForMission, null);
            zookeeperComponent.createPersistentNode(processing, null);
            //监听服务器是否掉线，如果掉线，取出他的任务给别的服务器
            zookeeperComponent.getData(delayQueueServer + serverPort, event -> {
                LOGGER.info("state:{},path:{},type:{}", event.getState(), event.getPath(), event.getType());
                if (event.getType() == Watcher.Event.EventType.NodeDeleted) {
                    try {
                        List<String> waitForMissions = zookeeperComponent.getChildren(waitForMission);
                        List<String> processings = zookeeperComponent.getChildren(processing);
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        zookeeperComponent.deleteNode(waitForMission);
                        zookeeperComponent.deleteNode(processing);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    }
                }
            }, null);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
