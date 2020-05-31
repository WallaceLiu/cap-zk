package com.bdata.cap.zklock;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.net.InetAddress;
import java.util.Collections;

/**
 * @author: liuning11
 * @create: 2020/05/31
 */
public class LockFactory {
    // 创建ZooKeeper对象
    public static final ZooKeeper DEFAULT_ZOOKEEPER = getDefaultZookeeper();

    // data格式: ip:stat 如: 192.168.1.107:lock or 192.168.1.107:unlock
    public static synchronized Lock getLock(String path, String ip) throws Exception {
        if (DEFAULT_ZOOKEEPER != null) {
            Stat stat = null;
            try {
                // 节点存在返回stat，进一步处理；否则返回null，创建新的节点
                stat = DEFAULT_ZOOKEEPER.exists(path, true);
            } catch (Exception e) {
                // TODO: use log system and throw new exception
            }
            if (stat != null) {
                byte[] data = DEFAULT_ZOOKEEPER.getData(path, null, stat);
                String dataStr = new String(data);
                String[] ipv = dataStr.split(":");
                // 如果节点存储的值等于当前传入的ip，则返回锁
                if (ip.equals(ipv[0])) {
                    Lock lock = new Lock(path);
                    lock.setZooKeeper(DEFAULT_ZOOKEEPER);
                    return lock;
                }
                // is not your lock, return null
                else {
                    return null;
                }
            }
            // no lock created yet, you can get it
            else {
                createZnode(path);
                Lock lock = new Lock(path);
                lock.setZooKeeper(DEFAULT_ZOOKEEPER);
                return lock;
            }
        }
        return null;
    }

    private static ZooKeeper getDefaultZookeeper() {
        try {
            ZooKeeper zooKeeper = new ZooKeeper("192.168.183.101:2181,192.168.183.102:2181,192.168.183.103:2181", 3000,
                    new Watcher() {
                        public void process(WatchedEvent event) {
                            System.out.println("event: " + event.getType());
                        }
                    });
            while (zooKeeper.getState() != ZooKeeper.States.CONNECTED) {// 等待连接成功
                Thread.sleep(3000);
            }
            return zooKeeper;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void createZnode(String path) throws Exception {
        if (DEFAULT_ZOOKEEPER != null) {
            InetAddress address = InetAddress.getLocalHost();
            String data = address.getHostAddress() + ":unlock";
            DEFAULT_ZOOKEEPER.create(path, data.getBytes(),
                    Collections.singletonList(new ACL(ZooDefs.Perms.ALL, ZooDefs.Ids.ANYONE_ID_UNSAFE)), CreateMode.EPHEMERAL);
        }
    }
}
