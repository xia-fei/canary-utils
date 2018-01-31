package com.qccr.saas.wing.insight.test;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MockAspectTest  extends SpringTest{
    @Autowired
    MockFacade facade;
    @Test
    public void test1(){
        System.out.println(JSON.toJSONString(facade.list()));
    }

    @Test
    public void test2(){
        System.out.println(JSON.toJSONString(facade.list2()));
    }
}
