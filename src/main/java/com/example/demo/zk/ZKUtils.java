package com.example.demo.zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import java.util.concurrent.CountDownLatch;

/**
 * zk工具类
 */
public class ZKUtils {

    private static ZooKeeper zk ;
    private static String connStr  = "192.168.19.18:2181,192.168.19.28:2181,192.168.19.38:2181/test";
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static int sessionTime = 1000;

    public static ZooKeeper connZk(String connStr,int time) {
        try{
            zk = new ZooKeeper(connStr,time,getWatcher());
            countDownLatch.await();
        }catch (Exception e){
            System.out.println("connErr  "+e.getMessage());
        }
        return zk;
    }

    public static ZooKeeper connZk() {
       return connZk(connStr,sessionTime);
    }

    public static void closeZk() {
        //不关闭再次连接会出现异常
        if(zk != null) {
            try {
                zk.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * zk连接监听
     * Disconnected	        断开连接（与集群中的任何一台断开连接）时的状态
     * SyncConnected	    建立连接，这时的事件状态
     * AuthFailed	        客户端进行连接认证失败时
     * ConnectedReadOnly	连接到的zk服务是只读的，这时的客户端只可以进行读操作，而不能进行写操作
     * SaslAuthenticated	用于通知客户端它们是SASL认证的
     * Expired	            客户端与zk服务端建立连接后每隔一定时间会发送一次心跳检测，当心跳检测没有收到服务端的响应时即认定断开连接，session失效，此时的事件状态就是Expired，如果客户端想访问服务端，需要重新建立连接。
     * @return
     */
    private static Watcher  getWatcher(){
        Watcher watcher = new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                Event.KeeperState state= watchedEvent.getState();
                //异步回复之后才算是建立了连接
                switch (state) {
                    case SyncConnected:
                        System.out.println("SyncConnected........");
                        countDownLatch.countDown();
                        break;
                    case Disconnected:
                        System.err.println("Disconnected........");
                        countDownLatch =  new CountDownLatch(1);
                        break;
                    case Expired:
                        System.err.println("Expired........");
                        break;
                }
            }
        };
        return watcher;
    }
}
