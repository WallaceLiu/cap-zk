package com.bdata.cap.rmi.server;

import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.util.concurrent.CountDownLatch;

/**
 * @author: liuning11
 * @create: 2020/05/31
 */
public class ServiceProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceProvider.class);
    private CountDownLatch latch = new CountDownLatch(1);// 用于等待 SyncConnected 事件触发后继续执行当前线程

    public void publish(Remote remote, String host, int port) {// 发布RMI服务并注册RMI地址到ZooKeeper中
        String url = publishService(remote, host, port); // 发布 RMI 服务并返回 RMI 地址
        if (url != null) {
            ZooKeeper zk = connectServer(); // 连接 ZooKeeper 服务器并获取 ZooKeeper 对象
            if (zk != null) {
                createNode(zk, url); // 创建 ZNode 并将 RMI 地址放入 ZNode 上
            }
        }
    }

    private String publishService(Remote remote, String host, int port) {//发布RMI服务
        String url = null;
        try {
            url = String.format("rmi://%s:%d/%s", host, port, remote.getClass()
                    .getName());
            LocateRegistry.createRegistry(port);
            Naming.rebind(url, remote);
            LOGGER.debug("publish rmi service (url: {})", url);
        } catch (Exception e) {
            LOGGER.error("", e);
        }
        return url;
    }

    private ZooKeeper connectServer() {// 连接 ZooKeeper 服务器
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(Constant.ZK_CONNECTION_STRING, Constant.ZK_SESSION_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        latch.countDown(); // 唤醒当前正在执行的线程
                    }
                }
            });
            latch.await(); // 使当前线程处于等待状态
        } catch (Exception e) {
            LOGGER.error("", e);
        }
        return zk;
    }

    private void createNode(ZooKeeper zk, String url) {// 创建 ZNode
        try {
            byte[] data = url.getBytes();
            // 创建一个临时性且有序的 ZNode
            String path = zk.create(Constant.ZK_PROVIDER_PATH, data,
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL);
            LOGGER.debug("create zookeeper node ({} => {})", path, url);
        } catch (Exception e) {
            LOGGER.error("", e);
        }
    }
}
