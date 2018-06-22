package com.wing.insight.test.thread;

import com.wing.core.thread.HeavyThreadExecution;
import org.junit.Test;

import java.util.Comparator;
import java.util.Random;

/**
 * 解决下面这种需求
 * 处理平凡的更新
 *
 * @author 夏飞
 */
public class HeavyThreadExecutionTest {
    @Test
    public void main() {
        //定义任务优先级
        Comparator<InsertDBRunnable> comparator = new Comparator<InsertDBRunnable>() {
            @Override
            public int compare(InsertDBRunnable o1, InsertDBRunnable o2) {
                return Integer.compare(o1.getCount(), o2.getCount());
            }
        };
        //创建 慢任务处理器
        HeavyThreadExecution<InsertDBRunnable> execution = new HeavyThreadExecution<>(comparator);

        for (int i = 0; i < 30; i++) {
            try {
                Thread.sleep(new Random().nextInt(50));
            } catch (InterruptedException ignored) {
            }
            System.out.println("当前用户访问人数:" + i);
            //模拟业务平凡插入
            execution.execute(new InsertDBRunnable(i));
        }

        //由于 用junit测试 等待线程池执行完
        execution.getThreadPoolExecutor().shutdown();
        while (!execution.getThreadPoolExecutor().isTerminated()) {

        }
        System.out.println("线程池任务执行完成");
    }

}
