package com.qccr.saas.wing.insight;

import com.google.common.collect.Lists;
import com.qccr.knife.result.CommonStateCode;
import com.qccr.knife.result.Results;
import com.qccr.saas.wing.core.mock.MockDataGenerate;
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


    @Around(value = QUERY_POINTCUT_EL)
    public Object doQueryAround(final ProceedingJoinPoint joinPoint) throws Exception {
        Method method = MethodSignature.class.cast(joinPoint.getSignature()).getMethod();
        Object[] args = joinPoint.getArgs();
        Mock annotation = method.getAnnotation(Mock.class);
        //获取方法的泛型类型
        MockDataGenerate mockDataGenerate =new MockDataGenerate();
        try {
            Type actualClass=getActualClass(method);
            Object mockObject=null;
            if(Class.class.isInstance(actualClass)){
                mockObject= mockDataGenerate.generateMock((Class<?>) actualClass);
            }else {
                ParameterizedType parameterizedType= (ParameterizedType) actualClass;
                if(isList(parameterizedType)){
                    Class itemClass= (Class) parameterizedType.getActualTypeArguments()[0];
                    List list= Lists.newArrayList();
                    for (int i = 0; i < 5; i++) {
                        list.add(mockDataGenerate.generateMock(itemClass));
                    }
                    mockObject=list;
                }else {
                    throw new RuntimeException(actualClass.toString());
                }

            }
            LOG.info("mock成功,method:{},result:{}",method.getName());
            return Results.newSuccessResult(mockObject,"mock数据");
        }catch (RuntimeException e){
            LOG.error("mock失败",e);
            return Results.newFailedResult(CommonStateCode.INNER_SERVER_ERROR,"mock数据生成失败");
        }
    }
    private boolean isList(ParameterizedType parameterizedType){
        return List.class.isAssignableFrom((Class)parameterizedType.getRawType());
    }

    private Type getActualClass(Method method){
        ParameterizedType parameterizedType = (ParameterizedType) method.getGenericReturnType();
        return  parameterizedType.getActualTypeArguments()[0];
    }


}
