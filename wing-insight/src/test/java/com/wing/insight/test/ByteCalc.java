package com.wing.insight.test;

import java.util.concurrent.atomic.AtomicInteger;

public class ByteCalc {

    static final int COUNT_BITS = Integer.SIZE - 3;
    static final int CAPACITY   = (1 << COUNT_BITS) - 1;

    // runState is stored in the high-order bits
    static final int RUNNING    = -1 << COUNT_BITS;
    static final int SHUTDOWN   =  0 << COUNT_BITS;
    static final int STOP       =  1 << COUNT_BITS;
    static final int TIDYING    =  2 << COUNT_BITS;
    static final int TERMINATED =  3 << COUNT_BITS;

    static int runStateOf(int c)     { return c & ~CAPACITY; }
    static int workerCountOf(int c)  { return c & CAPACITY; }
    static int ctlOf(int rs, int wc) { return rs | wc; }

    public static void main(String[] args) {
        final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));
        System.out.println(RUNNING+" "+Integer.toBinaryString(RUNNING));
        System.out.println(SHUTDOWN+" "+Integer.toBinaryString(SHUTDOWN));
        System.out.println(STOP+" "+Integer.toBinaryString(STOP));
        System.out.println(TIDYING+" "+Integer.toBinaryString(TIDYING));
        System.out.println(TERMINATED+" "+Integer.toBinaryString(TERMINATED));

        System.out.println(CAPACITY+" "+Integer.toBinaryString(CAPACITY));
        System.out.println(~CAPACITY+" "+Integer.toBinaryString(~CAPACITY));
    }
}
