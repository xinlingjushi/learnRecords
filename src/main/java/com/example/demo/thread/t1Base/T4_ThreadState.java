package com.example.demo.thread.t1Base;

import java.util.*;

public class T4_ThreadState {
    //七个状态：源码提供6个，RUNNABLE又可以分为两种：CPU调度与挂起状态，分别为两种状态运行（RUNNING）和就绪（READY）。
    //具体流转和图例再脑图里：https://www.processon.com/mindmap/5f90ee5be401fd06fd9a49c0
//    public static Thread.State toThreadState(int var0) {
//        if ((var0 & 4) != 0) {
//            return Thread.State.RUNNABLE;
//        } else if ((var0 & 1024) != 0) {
//            return Thread.State.BLOCKED;
//        } else if ((var0 & 16) != 0) {
//            return Thread.State.WAITING;
//        } else if ((var0 & 32) != 0) {
//            return Thread.State.TIMED_WAITING;
//        } else if ((var0 & 2) != 0) {
//            return Thread.State.TERMINATED;
//        } else {
//            return (var0 & 1) == 0 ? Thread.State.NEW : Thread.State.RUNNABLE;
//        }
//    }
    public static void main(String argv[]){
        statTest1();
        productionAndConsumption();
    }

    private static class Production extends  Thread{
        Queue<Integer> b ;
        int num = 2;
        Production(Queue<Integer> queue){
            this.b=queue;
        }

        @Override
        public void run() {
            for (;;){
                synchronized(b){
                    while (b.size()>=num){
                        try {
                            b.wait();//产能溢出，等待消费"
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int pnum= new Random().nextInt(10);
                    b.offer(pnum);//生产 "+pnum
                    b.notifyAll();
                }
            }
        }
    }
    private static class Consumption extends  Thread{
        Queue<Integer> b ;
        int num = 2;
        Consumption(Queue<Integer> queue){
            this.b=queue;
        }
        @Override
        public void run() {
            for (;;){
                synchronized(b){
                    while (b.isEmpty()){
                        try {
                            b.wait();//生产供不应求，消费等待
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    b.poll();//消费---
                    b.notifyAll();
                }
            }
        }
    }

    /**
     * 生产消费模型中有对应的三个状态
     * BLOCKED,RUNNABLE,WAITING
     */
    private static void productionAndConsumption(){
        Queue<Integer> q = new LinkedList<>();
        Production p1 = new Production(q);
        Consumption c1 = new Consumption(q);
        Consumption c2 = new Consumption(q);
        new Thread(() -> {
            Set<Thread.State> ts = new LinkedHashSet<>();
            labe: for (;;){
                try {
                    Thread.sleep(500);
                    System.out.println(p1.getState());
                    ts.add(p1.getState());
                    if (ts.size()==3){
                        break labe;//拿到想要的三个状态后停止
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.exit(0);//关闭程序
        }).start();
        p1.start();
        c1.start();
        c2.start();
    }

    /**
     * 使用sleep（time）的测试体现出3个状态
     * NEW,TIMED_WAITING,TERMINATED
     */
    private static void statTest1(){
        Thread t1 = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        new Thread(() -> {
            labe: for (;;){
                try {
                    Thread.sleep(200);
                    System.out.println(t1.getState());
                    if(Thread.State.TERMINATED.equals(t1.getState())){
                        break labe;//线程死亡停止
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();
        System.err.println(t1.getState());
        t1.start();
        try {
            t1.join();//让t1先执行，模拟出终结
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
