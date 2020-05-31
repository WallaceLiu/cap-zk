package com.bdata.cap.zklock;

import java.net.InetAddress;

/**
 * @author: liuning11
 * @create: 2020/05/31
 */
public class TestZookeeper {
    public static void main(String[] args) throws Exception {
        InetAddress address = InetAddress.getLocalHost();
        Lock lock = LockFactory.getLock("/root/test", address.toString());
        while(true)
        {
            if (lock == null) {
                //to do
                System.out.println("伺机篡位");
            }
            else {
                System.out.println("我是老大");
//                Thread.sleep(60*1000);
            }
        }
    }
}
