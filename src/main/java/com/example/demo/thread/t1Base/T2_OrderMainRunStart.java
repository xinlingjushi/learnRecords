package com.example.demo.thread.t1Base;

import java.util.concurrent.TimeUnit;

public class T2_OrderMainRunStart {

    private static class T2 extends  Thread{
        @Override
        public void run() {
            for (int i=0;i<10;i++) {
                try {
                    TimeUnit.MICROSECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.err.println("T2");
            }
        }
    }

    public static void main(String[] args) {
        new T2().start();//T2，main交替执行，开了分只
//        new T2().run();//先T2，后main
        for (int i=0;i<10;i++) {
            try {
                TimeUnit.MICROSECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.err.println("mian");
        }
    }

}
