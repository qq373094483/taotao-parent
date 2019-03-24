package com.taotao.ordertimer.order.cancel;

import com.taotao.pojo.TbOrder;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class OrderCancelDelayed<T extends Runnable> implements Delayed {
    /**
     * 到期时间
     */
    private final long expireTime;
    private TbOrder tbOrder;

    /**
     * 问题对象
     */
    private final T orderCancelTask;
    private static final AtomicLong atomic = new AtomicLong(0);

    private final long n;

    public OrderCancelDelayed(T orderCancelTask, TbOrder tbOrder) {
        //30分钟后过期30*60*1000*1000
        this.expireTime = tbOrder.getCreateTime().getTime()+(30*60*1000);
        this.orderCancelTask = orderCancelTask;
        this.tbOrder = tbOrder;
        this.n = atomic.getAndIncrement();
    }

    /**
     * 返回与此对象相关的剩余延迟时间，以给定的时间单位表示
     */
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.expireTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed other) {
        if (other == this) // compare zero ONLY if same object
            return 0;
        if (other instanceof OrderCancelDelayed) {
            OrderCancelDelayed x = (OrderCancelDelayed) other;
            long diff = expireTime - x.expireTime;
            if (diff < 0)
                return -1;
            else if (diff > 0)
                return 1;
            else if (n < x.n)
                return -1;
            else
                return 1;
        }
        long d = (getDelay(TimeUnit.MILLISECONDS) - other.getDelay(TimeUnit.MILLISECONDS));
        return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
    }

    public T getOrderCancelTask() {
        return this.orderCancelTask;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof OrderCancelDelayed) {
            OrderCancelDelayed other = (OrderCancelDelayed) object;
            return other.tbOrder.getId().equals(this.tbOrder.getId());
        }
        return false;
    }
}
