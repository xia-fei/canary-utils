package com.qccr.saas.wing.insight.test;

import org.junit.Test;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;

public class BeanUtilsTest {

    @Test
    public void test1() {
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(Person.class);
        System.out.println(propertyDescriptors);
    }



}


