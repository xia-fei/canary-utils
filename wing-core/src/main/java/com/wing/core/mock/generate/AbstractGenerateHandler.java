package com.wing.core.mock.generate;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Random;

public abstract class AbstractGenerateHandler {
     protected String getRandom(String[] values) {
        return values[new Random().nextInt(values.length)];
    }


    /**
     * 是否可以生成对象
     */
    public abstract boolean canHandler(Class mockClass);

    /**
     * 自己生成值
     *
     * @return 对象实例
     */
    public abstract String defaultValue(Class clazz);

    /**
     * 根据 字段注解配置的值
     *
     * @return 字符串
     */
    public abstract Object mockInstance(String str, Class mockClass);



    Object generateValue(Class mockClass, String[] mockValues) {
        String mockValue;
        if (mockValues != null) {
            //使用字段配置
            mockValue = getRandom(mockValues);
        } else {
            //使用默认
            mockValue = defaultValue(mockClass);
        }
        return mockInstance(mockValue,mockClass);
    }

}
