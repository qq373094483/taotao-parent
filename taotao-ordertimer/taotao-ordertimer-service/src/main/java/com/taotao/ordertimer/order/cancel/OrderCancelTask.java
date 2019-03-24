package com.taotao.ordertimer.order.cancel;

import com.taotao.mapper.TbOrderMapper;
import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbOrderExample;

import java.util.Date;

public class OrderCancelTask implements Runnable {
    private TbOrderMapper tbOrderMapper;
    private TbOrder tbOrder;

    public OrderCancelTask(TbOrder tbOrder,TbOrderMapper tbOrderMapper) {
        this.tbOrder = tbOrder;
        this.tbOrderMapper = tbOrderMapper;
    }

    @Override
    public void run() {
        TbOrderExample tbOrderExample = new TbOrderExample();
        TbOrderExample.Criteria criteria = tbOrderExample.createCriteria();
        criteria.andStatusEqualTo(TbOrder.Status.ORDERS.getCode());
        criteria.andIdEqualTo(tbOrder.getId());
        criteria.andUpdateTimeEqualTo(tbOrder.getUpdateTime());

        tbOrder.setStatus(TbOrder.Status.CANCEL.getCode());
        tbOrder.setUpdateTime(new Date());
        tbOrder.setCloseTime(tbOrder.getUpdateTime());
        tbOrderMapper.updateByExampleSelective(tbOrder, tbOrderExample);
    }
}
