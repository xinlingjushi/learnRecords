package com.example.demo.zk.conf;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import java.util.concurrent.CountDownLatch;

public class WatchCallBack implements Watcher, AsyncCallback.StatCallback, AsyncCallback.DataCallback {

    private  ZooKeeper zk ;
    private  String confStr ;
    private  CountDownLatch countDownLatch;

    public  ZooKeeper getZk() {
        return zk;
    }

    public  String getConfStr() { return confStr; }

    public  CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public  void setZk(ZooKeeper zk) {this.zk = zk;}

    public  void setConfStr(String confStr) { this.confStr = confStr; }

    public  void setCountDownLatch(int i) { this.countDownLatch = new CountDownLatch(i); }

    //数据回调
    @Override
    public void processResult(int i, String s, Object o, byte[] bytes, Stat stat) {
        System.out.println("processResult---2---data----");
        if(bytes!=null){
            System.out.println("processResult---2----"+new String(bytes));
            setConfStr(new String(bytes));
            countDownLatch.countDown();
        }
    }

    //节点回调
    @Override
    public void processResult(int i, String s, Object o, Stat stat) {
        System.out.println("processResult---1----");
        if (stat != null) {
            System.out.println("processResult---1----getData---");
            zk.getData("/appConf",this,this,"appConf");
        }else{
            System.out.println("processResult---1---- 节点不存在");
        }
    }

    /**
     * 节点事件监听
     * NodeCreated	 节点被创建事件被触发
     * NodeChildrenChanged	节点的直接子节点发生变更时触发
     * NodeDataChanged	节点的数据变更时触发
     * NodeDeleted	节点被删除时触发
     * None	 客户端的连接状态发生变更时
     * @param watchedEvent
     */
    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("process----");
        switch (watchedEvent.getType()) {
            case None:
                System.err.println("None---户端的连接状态发生变更");
                setConfStr("");
                break;
            case NodeCreated:
                zk.getData("/appConf",this,this,"NodeCreated");
                break;
            case NodeDeleted:
                //节点被删除，根据数据一致性的容忍性决定实现方式
                System.out.println("数据被删除");
                setConfStr("");
                setCountDownLatch(1);
                break;
            case NodeDataChanged:
                //数据被变更，重新获取
                zk.getData("/appConf",this,this,"NodeDataChanged");
                break;
        }
    }

    public void aWait(){
        zk.exists("/appConf", this, this,"aWait");
        try {
            countDownLatch.await();//等待监听回调操作
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
