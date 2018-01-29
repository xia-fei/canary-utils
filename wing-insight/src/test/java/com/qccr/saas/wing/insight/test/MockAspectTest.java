package com.qccr.saas.wing.insight.test;

import com.qccr.knife.result.Result;
import com.qccr.knife.result.Results;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Method;
import java.util.List;

public class MockAspectTest {
    @Test
    public void test1(){
        Method method=BeanUtils.findDeclaredMethod(MockAspectTest.class,"getResult",new Class[]{});
        System.out.println(method);
    }

    Result<List<String>> getResult(){
        return Results.newResult(null);
    }
}
