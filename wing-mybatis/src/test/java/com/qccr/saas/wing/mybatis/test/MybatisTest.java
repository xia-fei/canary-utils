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

public class MybatisTest {
    @Test
    public void testSqlSession(){
        PooledDataSource dataSource =new PooledDataSource();
        dataSource.setUsername("toowell");
        dataSource.setPassword("toowell2013db");
        dataSource.setUrl("jdbc:mysql://192.168.5.122:3306/test");
        dataSource.setDriver("com.mysql.jdbc.Driver");

        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(BlogMapper.class);
        configuration.addInterceptor(new MyInterceptor());

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        SqlSession sqlSession=sqlSessionFactory.openSession();
        BlogMapper blogMapper=sqlSession.getMapper(BlogMapper.class);
        /*System.out.println(blogMapper.selectValue("156","dsd"));*/


        Parameters parameters=new Parameters();
        parameters.setIsReward(true);
        System.out.println(blogMapper.selectBoole(parameters));
        parameters.setIsReward(false);
        System.out.println(blogMapper.selectBoole(parameters));

    }
}
