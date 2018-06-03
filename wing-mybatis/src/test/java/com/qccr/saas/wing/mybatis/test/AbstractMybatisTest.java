package com.qccr.saas.wing.mybatis.test;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;

abstract public class AbstractMybatisTest {
    SqlSession sqlSession;
    Mapper mapper;
    @Before
    public void init() throws IOException {
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("mybatis-config.xml"));
        sqlSession=sqlSessionFactory.openSession(true);
        mapper=sqlSession.getMapper(Mapper.class);
    }


}
