package com.example.demo.zk.conf;

import com.example.demo.zk.ZKUtils;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Random;

public class TestConfig {

    private static ZooKeeper zk ;
    private static String confStr ;

    @Before
    public  void conn(){
        zk = ZKUtils.connZk();
    }

    @After
    public  void  close(){ ZKUtils.closeZk(); }

    @Test
    public  void getConf(){
        //exists 有同步和异步的
        //把监听和回调提炼处理：如果单独写，Watcher和Callback套的比较多
        WatchCallBack watchCallBack = new WatchCallBack();
        watchCallBack.setZk(zk);
        watchCallBack.setCountDownLatch(1);
        watchCallBack.setConfStr(this.confStr);
        watchCallBack.aWait();
        //1.节点不存在，获取数据的没有执行，程序一直在等待。别人创建后，监听开始调用，需要获取数据
        //2.节点存在，通过回调获取数据
        for (;;){
            if("".equals(this.confStr)|| null == this.confStr){
                System.err.println("Conf is null...");
                watchCallBack.aWait();
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

}
