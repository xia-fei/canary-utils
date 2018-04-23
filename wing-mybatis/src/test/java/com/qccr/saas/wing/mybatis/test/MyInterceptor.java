package com.qccr.saas.wing.mybatis.test;

import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;
@Intercepts(
        {@Signature(
        type= Executor.class,
        method = "query",
        args = {MappedStatement.class,Object.class, RowBounds.class, ResultHandler.class})})
public class MyInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object paramObject=invocation.getArgs()[1];
        MapperMethod.ParamMap paramMap= (MapperMethod.ParamMap) paramObject;
        paramMap.put("userId",false);
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
