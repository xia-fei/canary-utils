package com.wing.insight.test.thread;

import com.wing.core.thread.ParallelThreadExecute;
import org.junit.Test;

/**
 * @author 夏飞
 */
public class ParallelThreadExecuteTest {
    @Test
    public void testParallel(){
        ParallelThreadExecute parallelThreadExecute=new ParallelThreadExecute();
        parallelThreadExecute.run(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("11");
            }
        });
        parallelThreadExecute.run(new Runnable() {
            @Override
            public void run() {
                System.out.println("22");
            }
        });
        parallelThreadExecute.run(new Runnable() {
            @Override
            public void run() {
                System.out.println("33");
            }
        });
        parallelThreadExecute.await();
        System.out.println("全部执行完成");

    }
}
