package com.qccr.saas.wing.core.mock;

import com.google.common.collect.Lists;
import com.qccr.saas.wing.core.mock.generate.DataGenerateFactory;
import com.qccr.saas.wing.facade.mock.MockValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class MockData {

    private final static Logger LOGGER = LoggerFactory.getLogger(MockData.class);
    private final DataGenerateFactory DATA_GENERATE_FACTORY;


    private int listSize = 3;


    public MockData() {
        DATA_GENERATE_FACTORY = new DataGenerateFactory();
    }

    public void setListSize(int listSize) {
        this.listSize = listSize;
    }

    public Object mock(Type type) {
        return generateTree(type, null);
    }


    private Object generateTree(Type type, Field field) {
        if (Class.class.isInstance(type)) {
            Class typeClass = (Class) type;
            Object mockValue = DATA_GENERATE_FACTORY.generateValue(typeClass, getFieldValues(field));
            if (mockValue != null) {
                return mockValue;
            } else {
                return generateObject(typeClass);
            }
        } else if (isListClass(type)) {
            List<Object> list = Lists.newArrayList();
            for (int i = 0; i < listSize; i++) {
                list.add(generateTree(getListClass(type), field));
            }
            return list;
        }
        throw new IllegalArgumentException("未能转换的类型:" + String.valueOf(type));
    }

    private Class getListClass(Type type) {
        ParameterizedType parameterizedType = (ParameterizedType) type;
        return (Class) parameterizedType.getActualTypeArguments()[0];
    }

    private boolean isListClass(Type type) {
        if (ParameterizedType.class.isInstance(type)) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            return List.class.isAssignableFrom((Class<?>) parameterizedType.getRawType());
        }
        return false;
    }


    private Object generateObject(Class clazz) {
        Object objMapper = BeanUtils.instantiate(clazz);
        for (Field field : clazz.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                field.set(objMapper, generateTree(field.getGenericType(), field));
            } catch (IllegalAccessException e) {
                LOGGER.error("不能set的字段", e);
            }
        }
        return objMapper;
    }

    private String[] getFieldValues(Field field) {
        if (field != null) {
            MockValue mockValue = field.getAnnotation(MockValue.class);
            if (mockValue != null) {
                return mockValue.value();
            }
        }
        return null;

    }


}
