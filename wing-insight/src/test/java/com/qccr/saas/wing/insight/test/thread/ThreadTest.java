package com.qccr.saas.wing.insight.test.thread;

import org.junit.Test;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadTest {

    @Test
    public void control(){
        outer: for (int i = 0; i < 10; i++) {

            for (int j = 0; j < 10; j++) {

                if (j > i) {
                    System.out.println();
                    break ;
                }

                System.out.print(i+"X"+j+"  " );
            }
        }
        System.out.println();
    }
    @Test
    public void testSet(){
        String[] s1=new String[]{"s1"};
        String[] s2=s1;
        s2[0]="s2";
        System.out.println(s1[0]);
    }
    @Test
    public void testThreadPool() throws InterruptedException {
        final Byte lock= (byte) 0;
        ThreadPoolExecutor threadPoolExecutor=new ThreadPoolExecutor(1,1,0,TimeUnit.DAYS,new LinkedBlockingDeque<Runnable>());
        for (int i = 0; i < 3; i++) {
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public  void run() {
                    synchronized (lock){
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });
        }

    }

    @Test
    public void testByte() {

    }

    @Test
    public void testQueue() throws InterruptedException {
        final SynchronousQueue<Integer> queue = new SynchronousQueue<Integer>();

        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("get" + queue.poll());
            }
        }.start();


        queue.put(1);
        System.out.println("PUT");


    }

    @Test
    public void test1() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.DAYS, new SynchronousQueue<Runnable>(true), new ThreadPoolExecutor.DiscardPolicy());

        for (int i = 0; i < 10; i++) {
            final int j = i;
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {

                    System.out.println(Thread.currentThread().getName() + "" + j);

                }
            });
        }
        System.out.println("ok");


    }
}
