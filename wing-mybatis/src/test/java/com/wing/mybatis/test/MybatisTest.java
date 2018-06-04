package com.wing.mybatis.test;

import org.junit.Test;

public class MybatisTest extends AbstractMybatisTest {
    @Test
    public void test(){
        System.out.println(mapper.test());
    }

    @Test
    public void testUpdate(){
        System.out.println(mapper.updateName("sss",3));
    }

    @Test
    public void insert(){
        System.out.println(mapper.addName("ddd"));
    }

    @Test
    public void insertSystemUser(){
        System.out.println(mapper.addSystemUser("ss",1));
    }
}
