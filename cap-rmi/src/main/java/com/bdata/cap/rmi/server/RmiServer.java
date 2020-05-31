package com.bdata.cap.rmi.server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

/**
 * @author: liuning11
 * @create: 2020/05/31
 */
public class RmiServer {
    public static void main(String[] args) throws Exception {
        int port = 1099;
        String url = "rmi://localhost:1099/demo.zookeeper.remoting.server.HelloServiceImpl";
        LocateRegistry.createRegistry(port);
        Naming.rebind(url, new HelloServiceImpl());
    }
}
