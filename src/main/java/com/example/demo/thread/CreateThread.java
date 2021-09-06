package com.example.demo.thread;

import java.io.DataInputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CreateThread    {


    public  class ct1 extends Thread{
        @Override
        public void run() {
            System.out.println("ct1");
        }
    }

    public  class ct2 implements Runnable{
        @Override
        public void run() {
          System.out.println("ct2");
        }
    }

    public  class ct3{
        public void  runs(){
            Executor executor = Executors.newFixedThreadPool(10);
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    System.out.println("ct3 executor");
                }
            };
            executor.execute(task);
        }

    }

}
