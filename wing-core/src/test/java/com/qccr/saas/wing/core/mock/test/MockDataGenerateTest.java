package com.qccr.saas.wing.core.mock.test;

import com.qccr.saas.wing.core.mock.MockDataGenerate;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Method;
import java.util.List;

public class MockDataGenerateTest {
    MockDataGenerate mockDataGenerate=new MockDataGenerate();
    @Test
    public void testGenerate(){
        Method method=BeanUtils.findDeclaredMethod(MockDataGenerateTest.class,"test");
        System.out.println(method.getReturnType());
    }

    public List<String> list(){
        return null;
    }
}
