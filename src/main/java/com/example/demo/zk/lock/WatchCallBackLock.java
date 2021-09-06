package com.example.demo.zk.lock;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class WatchCallBackLock implements Watcher ,AsyncCallback.StringCallback,AsyncCallback.ChildrenCallback ,AsyncCallback.StatCallback {

    private ZooKeeper zk ;
    private String  threadName;
    private CountDownLatch cdl= new CountDownLatch(1);
    private String  pathName;

    public ZooKeeper getZk() {
        return zk;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }


    public void getLock(){
        System.out.println("getLock...   " + threadName);
        try {
            //ACL：Access Control List  访问控制列表
            zk.create("/getLock",threadName.getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL,this,threadName);
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void clearLock(){
        System.out.println("clearLock...   " + threadName);
        try {
            zk.delete(pathName,-1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("process...   " + threadName);
        //有机器意外宕机，重新绑定
        switch (watchedEvent.getType()) {
            case NodeDeleted:
                zk.getChildren("/",false,this ,"sdf");
                break;
        }
    }

    @Override
    public void processResult(int i, String s, Object o, String s1) {
        System.out.println( " processResult  create node : " +  s1 +"   "+threadName);
        if(s1 != null ){
            pathName =  s1 ;
            zk.getChildren("/",false,this ,"sdf");
        }
    }

    //getChildren
    @Override
    public void processResult(int i, String s, Object o, List<String> list) {
        System.out.println("  processResult : " +  threadName );
        Collections.sort(list);
        int num = list.indexOf(pathName.substring(1));
        if(num == 0){
            cdl.countDown();
        }else {
            zk.exists("/"+list.get(num-1),this,this,"ex");
        }

    }

    //exists
    @Override
    public void processResult(int i, String s, Object o, Stat stat) {
        System.out.println("processResult---1----"+threadName);
        if (stat != null) {
            System.out.println("processResult---1----exists---"+threadName);
        }
    }
}
