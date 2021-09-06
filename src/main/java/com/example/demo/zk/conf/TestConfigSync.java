package com.example.demo.zk.conf;

import com.example.demo.zk.ZKUtils;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class TestConfigSync implements Watcher {

    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static ZooKeeper zk ;
    private static String confStr ;

    @Before
    public  void conn(){
        zk = ZKUtils.connZk();
    }

    @After
    public  void close() { ZKUtils.closeZk(); }

    @Test
    public  void getConf(){
        setData();
        for (;;){
            if("".equals(this.confStr)|| null == this.confStr){
                System.err.println("Conf is null...");
                setData();
            }else{
                System.out.println(this.confStr+"   " + new Random().nextInt());
            }

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private  void setData(){
        Stat exists;
        try {
            exists = zk.exists("/appConf", this);
            if(exists != null){
                byte[] data = zk.getData("/appConf", this, exists);
                this.confStr = new String(data);
                System.out.println(this.confStr);
            }
        } catch (Exception e) {
            System.err.println("err :  {}"+e.getMessage());
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("process----");
        switch (watchedEvent.getType()) {
            case None:
                System.err.println("None---");
                this.confStr = "";
                break;
            case NodeCreated:
                System.err.println("NodeCreated---");
                break;
            case NodeDeleted:
                System.err.println("NodeDeleted---");
                this.confStr = "";
                countDownLatch = new CountDownLatch(1);
                break;
            case NodeDataChanged:
                System.err.println("NodeDataChanged---");
                setData();
                break;
        }
    }
}
