package com.wing.insight.test.thread;

/**
 * @author 夏飞
 */
public class InsertDBRunnable implements Runnable {
    private int count;

    public InsertDBRunnable(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(300);
            System.out.println("当前人数插入DB:" + count);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
