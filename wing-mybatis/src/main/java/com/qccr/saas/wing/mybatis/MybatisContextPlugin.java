package com.qccr.saas.wing.mybatis;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

@Intercepts({
        @Signature(method = "deferLoad", type = Executor.class, args = {
                MappedStatement.class, Object.class, RowBounds.class,
                ResultHandler.class }),
        @Signature(method = "update", type = Executor.class, args = { MappedStatement.class, Object.class })
})
public class MybatisContextPlugin implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MetaObject mappedStatement = (MetaObject) invocation.getArgs()[2];
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return null;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
