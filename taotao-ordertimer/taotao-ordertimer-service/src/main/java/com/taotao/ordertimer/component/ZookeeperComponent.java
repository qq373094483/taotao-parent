package com.taotao.ordertimer.component;


import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

@Component
public class ZookeeperComponent implements Watcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperComponent.class);
    @Value("${ZOOKEEPER.IPANDPORT}")
    private String zookeeperIpAndPort;
    private ZooKeeper zooKeeper;
    private static final int SESSION_TIME_OUT = 20000;
    /**
     * 连接zookeeper为异步线程，所以要使用他变成同步
     */
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    @Override
    public void process(WatchedEvent event) {
        if (event.getState() == Event.KeeperState.SyncConnected) {
            System.out.println("Watch received event");
            countDownLatch.countDown();
        }
    }

    @PostConstruct
    public void connectZookeeper() throws Exception {
        zooKeeper = new ZooKeeper(zookeeperIpAndPort, SESSION_TIME_OUT, this);
        countDownLatch.await();
        System.out.println("zookeeper connection success");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                zooKeeper.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));
    }

    public Stat get(String path) throws KeeperException, InterruptedException {
        return zooKeeper.exists(path,false);
    }

    public ZooKeeper getZookeeper() {
        return zooKeeper;
    }

    /**
     * 创建节点
     *
     * @param path    
     * @param data    
     * @throws Exception    
     */
    public String createPersistentNode(String path, String data) throws KeeperException, InterruptedException {
        String s = createNode(path, data,CreateMode.PERSISTENT);
        if (s != null)
        {
            return s;
        }
        return null;
    }

    private String createNode(String path, String data,CreateMode createMode) throws KeeperException, InterruptedException {
        List<String> paths = getPaths(path);
        byte[] dateBytes = StringUtils.isNotBlank(data) ? data.getBytes() : null;
        for (int i=0;i<paths.size();i++) {
            String fullPath = StringUtils.join(paths.subList(0, i), "") + paths.get(i);
            if (this.isNotExists(fullPath)) {
                CreateMode createModeTemp = createMode;
                if (i<paths.size()-1&&(createMode == CreateMode.EPHEMERAL||createMode == CreateMode.EPHEMERAL_SEQUENTIAL||createMode == CreateMode.PERSISTENT_SEQUENTIAL)) {
                    createModeTemp = CreateMode.PERSISTENT;
                }
                String s = this.zooKeeper.create(fullPath, i == paths.size() - 1?dateBytes:null, ZooDefs.Ids.OPEN_ACL_UNSAFE, createModeTemp);
                LOGGER.info("data:{}",s);
                if (i == paths.size() - 1) {
                    return s;
                }
            }
        }
        return null;
    }

    private List<String> getPaths(String path){
        String[] paths= path.split("/");
        return Arrays.stream(paths).filter(pathTemp -> StringUtils.isNotBlank(pathTemp)).map(pathTemp->"/"+pathTemp).collect(Collectors.toList());
    }

    public String createPersistentSequentialNode(String path, String data) throws KeeperException, InterruptedException {
        String s = createNode(path, data,CreateMode.PERSISTENT_SEQUENTIAL);
        if (s != null)
        {
            return s;
        }
        return null;
    }

    public String createEphemeralNode(String path, String data) throws KeeperException, InterruptedException {
        String s = createNode(path, data,CreateMode.EPHEMERAL);
        if (s != null)
        {
            return s;
        }
        return null;
    }

    public String createEphemeralSequentialNode(String path, String data) throws KeeperException, InterruptedException {
        String s = createNode(path, data,CreateMode.EPHEMERAL_SEQUENTIAL);
        if (s != null)
        {
            return s;
        }
        return null;
    }

    /**
     *     * 获取路径下所有子节点
     *     * @param path
     *     * @return
     *     * @throws KeeperException
     *     * @throws InterruptedException
     *    
     */
    public List<String> getChildren(String path) throws KeeperException, InterruptedException {
        List<String> children = zooKeeper.getChildren(path, false);
        return children;
    }

    /**
     *     * 获取节点上面的数据
     *     * @param path  路径
     *     * @return
     *     * @throws KeeperException
     *     * @throws InterruptedException
     *    
     */
    public String getData(String path, Watcher watcher, Stat stat) throws KeeperException, InterruptedException {
        byte[] data = zooKeeper.getData(path, watcher, stat);
        if (data == null) {
            return "";
        }
        return new String(data);
    }

    /**
     *     * 获取节点上面的数据
     *     * @param path  路径
     *     * @return
     *     * @throws KeeperException
     *     * @throws InterruptedException
     *    
     */
    public String getData(String path, boolean watch, Stat stat) throws KeeperException, InterruptedException {
        byte[] data = zooKeeper.getData(path, watch, stat);
        if (data == null) {
            return "";
        }
        return new String(data);
    }

    /**
     *     * 获取节点上面的数据
     *     * @param path  路径
     *     * @return
     *     * @throws KeeperException
     *     * @throws InterruptedException
     *    
     */
    public void getData(String path, boolean watch, AsyncCallback.DataCallback cb, Object ctx) throws KeeperException, InterruptedException {
        zooKeeper.getData(path, watch, cb, ctx);
    }

    /**
     *     * 获取节点上面的数据
     *     * @param path  路径
     *     * @return
     *     * @throws KeeperException
     *     * @throws InterruptedException
     *    
     */
    public void getData(String path, Watcher watcher, AsyncCallback.DataCallback cb, Object ctx) throws KeeperException, InterruptedException {
        zooKeeper.getData(path, watcher, cb, ctx);
    }

    /**
     *     * 设置节点信息
     *     * @param path  路径
     *     * @param data  数据
     *     * @return
     *     * @throws KeeperException
     *     * @throws InterruptedException
     *    
     */
    public Stat setData(String path, String data) throws KeeperException, InterruptedException {
        //version为-1时，表示不根据版本号去设置数据
        return setData(path, data, -1);
    }
    public Stat setData(String path, String data,Integer version) throws KeeperException, InterruptedException {
        Stat stat = zooKeeper.setData(path, data!=null?data.getBytes():null, version);
        return stat;
    }

    /**
     *     * 删除节点
     *     * @param path
     *     * @throws InterruptedException
     *     * @throws KeeperException
     *    
     */
    public void deleteNode(String path) throws InterruptedException, KeeperException {
        zooKeeper.delete(path, -1);
    }

    /**
     * 节点是否存在
     *
     * @param path
     * @return
     * @throws KeeperException
     * @throws InterruptedException    
     */
    public boolean isExists(String path) throws KeeperException, InterruptedException {
        Stat stat = zooKeeper.exists(path, false);
        return stat != null;
    }

    /**
     * 节点是否存在
     *
     * @param path
     * @return
     * @throws KeeperException
     * @throws InterruptedException    
     */
    public boolean isNotExists(String path) throws KeeperException, InterruptedException {
        return !isExists(path);
    }

    /**
     *  获取创建时间
     *  @param path
     *  @return
     *  @throws KeeperException
     *  @throws InterruptedException
     *    
     */
    public String getCTime(String path) throws KeeperException, InterruptedException {
        Stat stat = zooKeeper.exists(path, false);
        return String.valueOf(stat.getCtime());
    }

    /**
     * 获取某个路径下孩子的数量
     *
     * @param path
     * @return
     * @throws KeeperException
     * @throws InterruptedException    
     */
    public Integer getChildrenNum(String path) throws KeeperException, InterruptedException {
        int childenNum = zooKeeper.getChildren(path, false).size();
        return childenNum;
    }
}
