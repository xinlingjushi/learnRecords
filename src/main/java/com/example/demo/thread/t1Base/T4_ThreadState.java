package com.example.demo.thread.t1Base;

public class T4_ThreadState {

    public static void main(String argv[]) throws InterruptedException {
        Thread t1 = new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    System.out.println("t1  sleep");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        });
        System.out.println(t1.getState());//NEW
        t1.start();
        System.out.println(t1.getState());//RUNNABLE
        t1.join();//让t1先执行
        System.out.println(t1.getState());//TERMINATED
    }

    //七个状态
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
}
