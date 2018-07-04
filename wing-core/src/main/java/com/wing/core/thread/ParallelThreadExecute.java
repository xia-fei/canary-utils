package com.wing.core.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author 夏飞
 * 并行处理器
 */
public class ParallelThreadExecute {
    private final Logger LOGGER = LoggerFactory.getLogger(ParallelThreadExecute.class);
    private List<Runnable> runnableList = new ArrayList<>();


    public void run(Runnable runnable) {
        runnableList.add(runnable);
    }

    public void await() {
        CountDownLatch countDownLatch = new CountDownLatch(runnableList.size());
        for (Runnable runnable : runnableList) {
            new Thread(new CountDownRunnable(runnable, countDownLatch)).start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            LOGGER.error("多线程异常结束", e);
        }
        runnableList.clear();
    }


    class CountDownRunnable implements Runnable {

        private Runnable runnable;
        private CountDownLatch countDownLatch;

        CountDownRunnable(Runnable runnable, CountDownLatch countDownLatch) {
            this.runnable = runnable;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                runnable.run();
            } catch (Throwable throwable) {
                LOGGER.error("任务执行异常", throwable);
            }
            countDownLatch.countDown();
        }
    }
}
