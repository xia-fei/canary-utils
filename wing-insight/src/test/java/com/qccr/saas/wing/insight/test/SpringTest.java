package com.qccr.saas.wing.insight.test;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-test.xml")
public class SpringTest {
    @Autowired
    Facade facade;

    @Test
    public void test1(){
        System.out.println(JSON.toJSONString(facade.list()));
    }
}
