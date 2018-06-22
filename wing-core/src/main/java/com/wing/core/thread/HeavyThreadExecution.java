package com.wing.core.thread;

import java.util.Comparator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 *  解决下面这种需求<br>
 *  处理频繁的更新的操作,只需要更新优先级最高的这种任务
 *  @author 夏飞
 */
public class HeavyThreadExecution<R extends Runnable> {
    private ThreadPoolExecutor threadPoolExecutor;


    public HeavyThreadExecution(Comparator<R> taskComparator) {
        this.threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(1), new PriorityRejectedExecutionHandler<>(taskComparator));
    }

    public void execute(R task) {
        this.threadPoolExecutor.execute(task);
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return this.threadPoolExecutor;
    }
}
