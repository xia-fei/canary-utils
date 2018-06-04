package com.wing.insight;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.qccr.commons.ro.PagedDataRO;
import com.qccr.knife.result.Result;
import com.qccr.knife.result.Results;
import com.wing.core.mock.MockData;
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
 *
 */
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MockAspect {
    private static final Logger LOG = LoggerFactory.getLogger(MockAspect.class);
    private static final String QUERY_POINTCUT_EL = "@annotation(Mock)";
    MockData mockData = new MockData();

    @Around(value = QUERY_POINTCUT_EL)
    public Object doQueryAround(final ProceedingJoinPoint joinPoint) throws Exception {
        Method method = MethodSignature.class.cast(joinPoint.getSignature()).getMethod();

        //获取方法的泛型类型
        Type methodReturnType = method.getGenericReturnType();
        Object result;
        Type resultType = getResultClass(methodReturnType);
        if (resultType != null) {
            Class pageDataClass = getPageClass(resultType);
            if (pageDataClass != null) {
                //page返回类型
                PagedDataRO pagedDataRO = new PagedDataRO();
                List<Object> list = Lists.newArrayList();
                for (int i = 0; i < 10; i++) {
                    list.add(mockData.mock(pageDataClass));
                }
                pagedDataRO.setResultList(list);
                pagedDataRO.setPageSize(list.size());
                pagedDataRO.setPageNo(1);
                pagedDataRO.setTotalSize(100);
                result = Results.newSuccessResult(pagedDataRO, "mock数据");
            } else {
                //正常result返回类型
                result = Results.newSuccessResult(mockData.mock(resultType), "mock数据");
            }

        } else {
            //非result返回类型
            result = mockData.mock(methodReturnType);
        }
        LOG.info("MOCK成功:{}", JSON.toJSONString(result));
        return result;
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


    private Type getResultClass(Type type) {
        if (ParameterizedType.class.isInstance(type)) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            if (Result.class.isAssignableFrom((Class<?>) parameterizedType.getRawType())) {
                return parameterizedType.getActualTypeArguments()[0];
            }
        }
        return null;
    }


}
