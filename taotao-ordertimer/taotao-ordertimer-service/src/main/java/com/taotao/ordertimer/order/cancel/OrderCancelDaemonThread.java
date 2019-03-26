package com.taotao.ordertimer.order.cancel;

import com.taotao.mapper.TbOrderMapper;
import com.taotao.pojo.TbOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * <p>
 * [任务调度系统]
 * <br>
 * [后台守护线程不断的执行检测工作]
 * </p>
 *
 * @author wangguangdong
 * @version 1.0
 * @Date 2015年11月23日14:19:40
 */
@Component
public class OrderCancelDaemonThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderCancelDaemonThread.class);
    Executor executor = Executors.newFixedThreadPool(100);
    /**
     * 创建一个最初为空的新 DelayQueue
     */
    private static final DelayQueue<OrderCancelDelayed> delayQueue = new DelayQueue<>();

    /**
     * 初始化守护线程
     */
    @PostConstruct
    public void init() {
        Thread daemonThread = new Thread(() -> execute());
        daemonThread.setDaemon(true);
        daemonThread.setName("Task Queue Daemon Thread");
        daemonThread.start();
    }

    private void execute() {
        System.out.println("start:" + System.currentTimeMillis());
        while (true) {
            try {
                //从延迟队列中取值,如果没有对象过期则队列一直等待，
                OrderCancelDelayed t1 = delayQueue.take();
                LOGGER.info("start");
                if (t1 != null) {
                    //修改问题的状态
                    Runnable task = t1.getOrderCancelTask();
                    if (task == null) {
                        continue;
                    }
                    executor.execute(task);
                    LOGGER.info("[at task:" + task + "]   [Time:" + System.currentTimeMillis() + "]");
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }



    /**
     * 添加任务，
     * time 延迟时间
     * task 任务
     * 用户为问题设置延迟时间
     */
    public void put(TbOrderMapper tbOrderMapper,TbOrder tbOrder) {
        //创建一个任务
        OrderCancelDelayed orderCancelDelayed = new OrderCancelDelayed(tbOrderMapper,tbOrder);
        //将任务放在延迟的队列中
        delayQueue.put(orderCancelDelayed);
    }

    /**
     * 结束订单
     * @param orderCancelDelayed
     */
    public boolean endTask(OrderCancelDelayed<Runnable> orderCancelDelayed){
        return delayQueue.remove(orderCancelDelayed);
    }
}