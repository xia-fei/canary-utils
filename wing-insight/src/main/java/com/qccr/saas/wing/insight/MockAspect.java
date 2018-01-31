package com.qccr.saas.wing.insight;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.qccr.commons.ro.PagedDataRO;
import com.qccr.knife.result.CommonStateCode;
import com.qccr.knife.result.Result;
import com.qccr.knife.result.Results;
import com.qccr.saas.wing.core.mock.MockData;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author yeWanQing
 * @since 2017/5/23
 */
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MockAspect {
    private static final Logger LOG = LoggerFactory.getLogger(MockAspect.class);
    private static final String QUERY_POINTCUT_EL = "@annotation(com.qccr.saas.wing.insight.Mock)";
    MockData mockData = new MockData();

    @Around(value = QUERY_POINTCUT_EL)
    public Object doQueryAround(final ProceedingJoinPoint joinPoint) throws Exception {
        Method method = MethodSignature.class.cast(joinPoint.getSignature()).getMethod();
        Object[] args = joinPoint.getArgs();
        Mock annotation = method.getAnnotation(Mock.class);
        //获取方法的泛型类型
        Type methodReturnType=method.getGenericReturnType();
        try {
            Object mockObject;
            Type resultType = getResultClass(methodReturnType);
            Preconditions.checkNotNull(resultType,"%s 不是result类型",methodReturnType.toString());
            Class pageDataClass = getPageClass(resultType);
            if (pageDataClass != null) {
                PagedDataRO pagedDataRO = new PagedDataRO();
                List<Object> list = Lists.newArrayList();
                for (int i = 0; i < 10; i++) {
                    list.add(mockData.mock(pageDataClass));
                }
                pagedDataRO.setResultList(list);
                pagedDataRO.setPageSize(list.size());
                pagedDataRO.setPageNo(1);
                pagedDataRO.setTotalSize(100);
                mockObject = pagedDataRO;
            } else {
                mockObject = mockData.mock(resultType);
            }
            LOG.info("mock成功,method:{},result:{}", method.getName());
            return Results.newSuccessResult(mockObject, "mock数据");
        } catch (RuntimeException e) {
            LOG.error("mock失败", e);
            return Results.newFailedResult(CommonStateCode.INNER_SERVER_ERROR, "mock数据生成失败");
        }
    }

    private Class getPageClass(Type type) {
        if (ParameterizedType.class.isInstance(type)) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            if (PagedDataRO.class.isAssignableFrom((Class<?>) parameterizedType.getRawType())) {
                return (Class) parameterizedType.getActualTypeArguments()[0];
            }
        }
        return null;
    }


    private Type getResultClass(Type type){
        if(ParameterizedType.class.isInstance( type)) {
            ParameterizedType parameterizedType= (ParameterizedType) type;
            if (Result.class.isAssignableFrom((Class<?>) parameterizedType.getRawType())) {
                 return parameterizedType.getActualTypeArguments()[0];
            }
        }
        return null;
    }


}
