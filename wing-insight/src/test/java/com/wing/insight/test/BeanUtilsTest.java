package com.wing.insight.test;

import com.wing.insight.test.bean.Person;
import net.sf.cglib.core.ReflectUtils;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;

public class BeanUtilsTest {


    @Test
    public void test1() {
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(Person.class);
        System.out.println(propertyDescriptors);
    }

    @Test
    public void test2(){
        PropertyDescriptor[] getters = ReflectUtils.getBeanGetters(Person.class);
        System.out.println(getters);
    }



}


