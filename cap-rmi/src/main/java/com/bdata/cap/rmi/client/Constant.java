package com.bdata.cap.rmi.client;

/**
 * @author: liuning11
 * @create: 2020/05/31
 */
public interface Constant {
    String ZK_CONNECTION_STRING = "192.168.1.201:2181,192.168.1.202:2181,192.168.1.203:2181";
    int ZK_SESSION_TIMEOUT = 5000;
    String ZK_REGISTRY_PATH = "/registry";
    String ZK_PROVIDER_PATH = ZK_REGISTRY_PATH + "/provider";
}
