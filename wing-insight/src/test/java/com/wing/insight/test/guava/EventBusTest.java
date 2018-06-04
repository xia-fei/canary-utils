package com.wing.insight.test.guava;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Executors;

public class EventBusTest {
    AsyncEventBus asyncEventBus=new AsyncEventBus(Executors.newFixedThreadPool(3));
    @Before
    public void init(){
        asyncEventBus.register(new ListenerOne());
        asyncEventBus.register(new ListenerTwo());
    }

    @Test
    public void testPost(){
        asyncEventBus.post("eventBus");
    }
    @After
    public void end() throws InterruptedException {

    }
}
class ListenerOne{
    @Subscribe
    @AllowConcurrentEvents
    public void say(String s){
        System.out.println(Thread.currentThread().getName()+getClass().getName()+" "+s);
    }
    @Subscribe
    @AllowConcurrentEvents
    public void say2(String s){
        System.out.println(Thread.currentThread().getName()+getClass().getName()+" "+s);
    }
}
class ListenerTwo{
    @Subscribe
    @AllowConcurrentEvents
    public void say(String s){
        System.out.println(Thread.currentThread().getName()+getClass().getName()+" "+s);
    }
}
