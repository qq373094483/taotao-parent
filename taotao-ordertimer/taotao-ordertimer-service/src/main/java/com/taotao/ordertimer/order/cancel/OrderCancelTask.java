package com.taotao.ordertimer.order.cancel;

import com.taotao.mapper.TbOrderMapper;
import com.taotao.ordertimer.component.ZookeeperComponent;
import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbOrderExample;
import org.apache.zookeeper.KeeperException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Date;

public class OrderCancelTask implements Runnable,ApplicationContextAware {
    private TbOrderMapper tbOrderMapper;
    private TbOrder tbOrder;
    private ApplicationContext applicationContext;

    public OrderCancelTask(TbOrder tbOrder,TbOrderMapper tbOrderMapper) {
        this.tbOrder = tbOrder;
        this.tbOrderMapper = tbOrderMapper;
    }

    @Override
    public void run() {
        String processing = "/delayQueue/orderCancel/server/processing";
        TbOrderExample tbOrderExample = new TbOrderExample();
        TbOrderExample.Criteria criteria = tbOrderExample.createCriteria();
        criteria.andStatusEqualTo(TbOrder.Status.ORDERS.getCode());
        criteria.andIdEqualTo(tbOrder.getId());
        criteria.andUpdateTimeEqualTo(tbOrder.getUpdateTime());

        tbOrder.setStatus(TbOrder.Status.CANCEL.getCode());
        tbOrder.setUpdateTime(new Date());
        tbOrder.setCloseTime(tbOrder.getUpdateTime());
        tbOrderMapper.updateByExampleSelective(tbOrder, tbOrderExample);
        ZookeeperComponent zookeeperComponent = applicationContext.getBean(ZookeeperComponent.class);
        try {
            zookeeperComponent.deleteNode(processing+"/"+tbOrder.getId());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
}
