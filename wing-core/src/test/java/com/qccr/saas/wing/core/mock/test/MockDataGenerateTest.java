package com.qccr.saas.wing.core.mock.test;

import com.qccr.saas.wing.core.mock.MockDataGenerate;
import com.qccr.saas.wing.core.mock.MockDate;
import com.qccr.saas.wing.core.mock.test.bean.Dog;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

public class MockDataGenerateTest {
    MockDataGenerate mockDataGenerate=new MockDataGenerate();
    @Test
    public void testGenerate(){
        Method method=BeanUtils.findDeclaredMethod(MockDataGenerateTest.class,"test");
        System.out.println(method.getReturnType());
    }


    @Test
    public void testMockDate(){
        MockDate mockDate=new MockDate();
        Dog dog= (Dog) mockDate.mock(Dog.class);
        System.out.println(Objects.toString(dog));
    }



    public List<String> list(){
        return null;
    }


}
