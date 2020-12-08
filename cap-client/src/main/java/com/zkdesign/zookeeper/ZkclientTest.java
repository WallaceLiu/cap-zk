package com.zkdesign.zookeeper;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.junit.Before;
import org.junit.Test;

/**
 * @author: liuning11@jd.com
 * @date: 2020/10/17
 */
@Slf4j
public class ZkclientTest {
    ZkClient zkClient;

    @Before
    public void init() {
        zkClient = new ZkClient("127.0.0.1:2181", 5000, 5000);
    }


    @Test
    public void createTest() throws InterruptedException {

        zkClient.subscribeDataChanges("/qiurunze", new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {
                log.info(s);
                log.info(JSON.toJSONString(o));
            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                log.info(s);
            }
        });

        Thread.sleep(100000);
    }
}
