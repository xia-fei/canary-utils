package com.wing.insight.test.mock;

import com.alibaba.fastjson.JSON;
import com.wing.core.mock.MockData;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class Main {
    @Test
    public void test1() throws ClassNotFoundException, MalformedURLException {
        String url="http://192.168.0.107:8081/nexus/service/local/repositories/snapshots/content/com/qccr/shprod/shprod-facade/3.9.9.2-SNAPSHOT/shprod-facade-3.9.9.2-20180727.081406-29.jar";
        URLClassLoader urlClassLoader=new URLClassLoader(new URL[]{new URL(url)});
        System.out.println(JSON.toJSONString(new MockData().mock(urlClassLoader.loadClass("com.qccr.shprod.facade.entity.businessCustomer.BusinessCustomerRO"))));
    }
}
