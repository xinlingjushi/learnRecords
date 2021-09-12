package com.example.demo.thread.t1Base;

import java.io.DataInputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class T1_CreateThread {

    /**
     *  继承Thread
     */
    public static class Ct1Thread extends Thread{
        @Override
        public void run() {
            System.out.println("Ct1Thread");
        }
    }

    /**
     *  实现Runnable接口
     */
    public static  class Ct2Runnable implements Runnable{
        @Override
        public void run() {
          System.out.println("Ct2Runnable");
        }
    }

    /**
     * 线程池Executor
     */
    public static class Ct3Executor{
        public void  runs(){
            Executor executor = Executors.newFixedThreadPool(10);
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    System.out.println("Ct3Executor");
                }
            };
            executor.execute(task);
        }
    }

    public static void main(String[] args) {
        new Ct1Thread().start();
        new Thread(new Ct2Runnable()).start();
        new Thread(()->{
            System.out.println("lamdba");
        }).start();
        new Ct3Executor().runs();
    }

}
