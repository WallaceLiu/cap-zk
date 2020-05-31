package com.zkdesign.zookeeper;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: liuning11
 * @date: 2020/5/29
 */
@Slf4j
public class DataTest {

    ZooKeeper zooKeeper;

    private static final String NODE_NAME = "/mynode";
    private String conn = "127.0.0.1:2181";
    private String cap_path = NODE_NAME + "/cap";

    @Before
    public void init() throws IOException {

        zooKeeper = new ZooKeeper(conn, 100000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("----------> watchedEvent Type:" + watchedEvent.getType());
            }
        });
    }

    /**
     * 删除节点，忽略版本
     */
    @Test
    public void testDelete() {
        try {
            zooKeeper.delete(cap_path, -1);
        } catch (Exception e) {
//            LOGGER.error(e.getMessage());
            //Assert.fail();
        }
    }

    /**
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void createNodeData() throws KeeperException, InterruptedException {
        List<ACL> list = new ArrayList<>();
        // acl
        int perm = ZooDefs.Perms.ADMIN | ZooDefs.Perms.ALL;
        ACL acl = new ACL(perm, new Id("world", "anyone"));
        list.add(acl);
        zooKeeper.create(cap_path, "hello".getBytes(), list, CreateMode.PERSISTENT);
    }

    /**
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void setNodeData() throws KeeperException, InterruptedException {
        Stat s1 = zooKeeper.setData(cap_path, "hello world 1".getBytes(), -1);
        System.out.println(s1.getCzxid() + " , " + s1.getMzxid() + " , " + s1.getVersion());
        Stat s2 = zooKeeper.setData(cap_path, "hello world 2".getBytes(), -1);
        System.out.println(s2.getCzxid() + " , " + s2.getMzxid() + " , " + s2.getVersion());
//        Stat s = zooKeeper.setData(cap_path, "hello world 3".getBytes(), s1.getVersion());
//        System.out.println(s.getCzxid() + " , " + s.getMzxid() + " , " + s.getVersion());
    }

    /**
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void getDataNull() throws KeeperException, InterruptedException {
        byte[] data = zooKeeper.getData(cap_path, null, null);
        System.out.println(new String(data));
    }

    /**
     * 获取数据 设置watch
     */
    @Test
    public void testGetDataWatch() {
        String result = null;
        // get data and watch
        try {
            byte[] bytes = zooKeeper.getData(cap_path, new Watcher() {
                public void process(WatchedEvent event) {
                    System.out.println("----------> Get Data Watch : " + event.getType());
                }
            }, null);
            result = new String(bytes);
        } catch (Exception e) {
            log.error(e.getMessage());
            //Assert.fail();
        }
        System.out.println("----------> Get Data result : " + result);

        // 改变节点数据，触发wacther
        System.out.println("----------> Begin to change znode");
        try {
            zooKeeper.setData(cap_path, "hello world".getBytes(), -1);
        } catch (Exception e) {
            log.error(e.getMessage());
            //Assert.fail();
        }
    }

    @Test
    public void getData4() throws KeeperException, InterruptedException {
        zooKeeper.getData(NODE_NAME, false, new AsyncCallback.DataCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
                log.info("stat:{}", JSON.toJSON(stat));
            }
        }, "");
        Thread.sleep(Long.MAX_VALUE);
    }

    /**
     * 判断节点是否存在，这个 watcher 是在创建 ZooKeeper实例时指定的 watcher
     */
    @Test
    public void testExists() {
        Stat stat = null;
        try {
            stat = zooKeeper.exists(cap_path, false);//false不监听
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println(stat.getCzxid());
    }

    /**
     * 判断节点是否存在, 设置是否监控这个目录节点，
     * 这里的 watcher 是在创建 ZooKeeper实例时指定的 watcher，
     * watcher会监控create/delete或者修改了节点值时触发
     */
    @Test
    public void testExistsWatch1() {
        Stat stat = null;
        // add watch
        try {
            stat = zooKeeper.exists(cap_path, true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        // change znode
        try {
            zooKeeper.delete(cap_path, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断节点是否存在, 设置监控这个目录节点的，watcher，watcher不会触发多次
     */
    @Test
    public void testExistsWatch2() {
        Stat stat = null;
        try {
            stat = zooKeeper.exists(cap_path, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    System.out.println("testExistsWatch2  watch : {}" + event.getType());
                }
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        // 触发watch 中的process方法 NodeDataChanged
        try {
            zooKeeper.setData(cap_path, "testExistsWatch2".getBytes(), -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 不会触发watch 只会触发一次
        try {
            zooKeeper.delete(cap_path, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void getChild() throws KeeperException, InterruptedException {
        List<String> children = zooKeeper.getChildren(NODE_NAME, false);
        children.stream().forEach(System.out::println);
    }

    /**
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void getChild2() throws KeeperException, InterruptedException {
        List<String> children = zooKeeper.getChildren(NODE_NAME, event -> {
            System.out.println(event.getPath());
            try {
                zooKeeper.getChildren(event.getPath(), false);
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        children.stream().forEach(System.out::println);
        Thread.sleep(Long.MAX_VALUE);
    }

    // ========================永久watch开始=========================


    /**
     * 永久watch测试，可用在配置管理，配置信息改变，通知客户端
     */
    @Test
    public void testRecvEvent() {
        String result = null;
        try {
            byte[] bytes = zooKeeper.getData("/zk001", setWatcher(), null);
            result = new String(bytes);
            System.out.println(result);
            while (true) {
                Thread.sleep(500);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
//            Assert.fail();
        }
    }

    private Watcher setWatcher() {
        return new Watcher() {
            String result = null;

            public void process(WatchedEvent event) {
                log.info("testRecvEvent  watch : {}", event.getType());
                System.out.println("I can do anything. ");
                try {
                    byte[] bytes = zooKeeper.getData("/zk001", setWatcher(), null);
                    result = new String(bytes);
                    System.out.println(result);
                } catch (Exception e) {
                    log.error(e.getMessage());
//                    Assert.fail();
                }
            }
        };
    }

    @After
    public void close() {
        if (zooKeeper != null) {
            try {
                zooKeeper.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
