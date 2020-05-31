package com.bdata.cap.rmi.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author: liuning11
 * @create: 2020/05/31
 */
public interface HelloService extends Remote {
    String sayHello(String name) throws RemoteException;
}
