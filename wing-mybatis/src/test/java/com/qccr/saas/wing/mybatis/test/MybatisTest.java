package com.qccr.saas.wing.mybatis.test;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
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
