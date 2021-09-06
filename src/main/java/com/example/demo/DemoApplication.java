package com.example.demo;

import org.apache.zookeeper.*;
import org.apache.zookeeper.AsyncCallback.ChildrenCallback;
import org.apache.zookeeper.data.Stat;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication
public class DemoApplication implements ChildrenCallback {

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public <watcher2> void main(String[] args) throws IOException, InterruptedException, KeeperException {

        SpringApplication.run(DemoApplication.class, args);

        String connStr  = "192.168.19.18:2181,192.168.19.28:2181,192.168.19.38:2181/appConf";
        Watcher watcher = new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                Event.KeeperState state= watchedEvent.getState();
                System.out.println("connWatcher  "+state.getIntValue());
                switch (state) {
                    case SyncConnected:
                        countDownLatch.countDown();
                        break;
                }
            }
        };
        ZooKeeper zk = new ZooKeeper(connStr,3000,watcher);
        countDownLatch.await();
        //连接之后在走以下操作
        ZooKeeper.States state = zk.getState();
        System.out.println("state    "+state.toString());
        Watcher watcher2 = new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                Event.EventType type= watchedEvent.getType();
                System.out.println("childrenWatcher  "+watchedEvent.getPath()+" "+type.toString());
            }
        };

        byte[] sb = "test".getBytes();
        //项目名称采用临时节点，
        zk.create("/project/projectName",sb, ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
        //查看某个项目下的接口，
        zk.getChildren("/project", false,this,"abc");

        while (true){

        }
    }




    private static void stateEvent(Watcher.Event.KeeperState state){
        System.out.println("stateEvent   "+state.toString());
        switch (state) {
            case SyncConnected:
                countDownLatch.countDown();
                break;
        }
    }

    private static void typeEvent(Watcher.Event.EventType type){
        switch (type) {
            case None:
                break;
            case NodeCreated:
                break;
            case NodeDeleted:
                break;
            case NodeDataChanged:
                break;
            case NodeChildrenChanged:
                break;
        }
    }

    @Override
    public void processResult(int i, String s, Object o, List<String> list) {

    }
}
