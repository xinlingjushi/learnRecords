package com.example.demo.zk.lock;

import com.example.demo.zk.ZKUtils;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * zk，分布式锁的实现
 */
public class TestLock {

    private static ZooKeeper zk ;
    private static String confStr ;
    private static String connStr  = "192.168.19.18:2181,192.168.19.28:2181,192.168.19.38:2181/lock";
    private static int sessionTime = 1000;

    @Before
    public  void conn(){
        zk = ZKUtils.connZk(connStr,sessionTime);
    }

    @After
    public  void close() { ZKUtils.closeZk(); }


    @Test
    public  void getConf(){
        for (int i = 0; i <5 ; i++) {
            new Thread(){
                @Override
                public void run() {
                    String threadName = Thread.currentThread().getName();
                    WatchCallBackLock wcbl = new WatchCallBackLock();
                    wcbl.setZk(zk);
                    wcbl.setThreadName(threadName);
                    //抢锁
                    wcbl.getLock();
                    //doWork
                    doWork(threadName);
                    //释放
                    wcbl.clearLock();
                }
            }.start();
        }
        for (;;){

        }
    }

    public  void  doWork(String threadName){
        System.err.println("doWork...   " + threadName);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
