package com.example.demo.thread.t1Base;

import java.util.concurrent.locks.LockSupport;

public class T3_ThreadMethod {

    /**
     * 睡眠:当前线程暂停一段时间，让给别的线程
     * 当前例子：每次都是下边那个线程先执行完
     */
    public static void sleepTest(){
        new Thread(()->{
            for (int i=0;i<10;i++) {
                try {
                    Thread.sleep(1000);
                    System.out.println("Thread  sleep");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        ).start();
        new Thread(()->{
            for (int i=0;i<10;i++) {
               System.out.println("Thread");
            }
        }
        ).start();
    }

    /**
     * 让出一下:返回就绪状态，进入等待队列
     * 当前例子：偶数时让出给就绪状态的线程执行，这个就绪状态的线程包括它自身
     */
    public static void yieldTest(){
        new Thread(()->{
            for (int i=0;i<10;i++) {
                if(i%10==0) {
                    System.err.println("Thread yield1  "+i);
                    Thread.yield();
                }else {
                    System.err.println("Thread yield1  "+i);
                }
            }
        }
        ).start();
        new Thread(()->{
            for (int i=0;i<10;i++) {
                if(i%10==0){
                    System.err.println("Thread yield2  "+i);
                    Thread.yield();
                }else{
                    System.out.println("Thread yield2  "+i);
                }
            }
        }
        ).start();
    }


    /**
     * 保证线程顺序：在自己线程内调用，等待别人结束，然后在接着运行
     * 当前例子：t1执行完，t2执行
     */
    public static void joinTest(){
        Thread t1 =   new Thread(()->{
            for (int i=0;i<10;i++) {
                try {
                    Thread.sleep(1000);
                    System.out.println("t1  sleep");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread t2 = new Thread(()->{
            for (int i=0;i<10;i++) {
                try {
                    t1.join();
                    System.out.println("t2");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
        t2.start();
    }


}
