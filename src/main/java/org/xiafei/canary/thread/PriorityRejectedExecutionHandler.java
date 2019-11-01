package org.xiafei.canary.thread;

import java.util.Comparator;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author 夏飞
 */
public class PriorityRejectedExecutionHandler<R extends Runnable> implements RejectedExecutionHandler {

    private Comparator<R> taskComparator;

    public PriorityRejectedExecutionHandler(Comparator<R> taskComparator) {
        this.taskComparator = taskComparator;
    }

    @Override
    public void rejectedExecution(Runnable recentRunnable, ThreadPoolExecutor executor) {
        Runnable queueRunnable = executor.getQueue().poll();
        if (queueRunnable != null) {
            if (this.taskComparator.compare((R) recentRunnable, (R) queueRunnable) > 0) {
                executor.getQueue().offer(recentRunnable);
            } else {
                executor.getQueue().offer(queueRunnable);
            }
        }
    }
}
