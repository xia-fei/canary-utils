package com.qccr.saas.wing.insight.test.thread;

import org.junit.Test;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadTest {

    @Test
    public void testQueue() throws InterruptedException {
        SynchronousQueue<Integer> queue=new SynchronousQueue<Integer>();
        for (int i = 0; i < 10; i++) {
            boolean b=queue.offer(1);
            System.out.println("offer "+b);
        }


    }

    @Test
    public void test1(){
        ThreadPoolExecutor threadPoolExecutor=new ThreadPoolExecutor(1,1,0, TimeUnit.DAYS, new SynchronousQueue<Runnable>(true),new ThreadPoolExecutor.DiscardPolicy());

        for (int i=0;i<10;i++){
            final int j=i;
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {

                    System.out.println(Thread.currentThread().getName()+""+j);

                }
            });
        }
        System.out.println("ok");




    }
}
