package com.qccr.saas.wing.insight.test;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MockAspectTest  extends SpringTest{
    @Autowired
    MockFacade mockFacade;
    @Test
    public void test6(){
        System.out.println(JSON.toJSONString(mockFacade.roTest()));
    }
    @Test
    public void test5(){
        System.out.println(JSON.toJSONString(mockFacade.list5()));
    }
    @Test
    public void test4(){
        System.out.println(JSON.toJSONString(mockFacade.list4()));
    }
    @Test
    public void test3(){
        System.out.println(JSON.toJSONString(mockFacade.list3()));
    }
    @Test
    public void test1(){
        System.out.println(JSON.toJSONString(mockFacade.list()));
    }

    @Test
    public void test2(){
        System.out.println(JSON.toJSONString(mockFacade.list2()));
    }
}
