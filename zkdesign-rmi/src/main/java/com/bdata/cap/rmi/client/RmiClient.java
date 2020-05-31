package com.bdata.cap.rmi.client;

import java.rmi.Naming;

/**
 * @author: liuning11
 * @create: 2020/05/31
 */
public class RmiClient {
    public static void main(String[] args) throws Exception {
        String url = "rmi://localhost:1099/demo.zookeeper.remoting.server.HelloServiceImpl";
        HelloService helloService = (HelloService) Naming.lookup(url);
        String result = helloService.sayHello("Jack");
        System.out.println(result);
    }
}
