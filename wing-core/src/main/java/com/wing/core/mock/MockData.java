package com.wing.core.mock;

import com.google.common.collect.Lists;
import com.wing.core.mock.generate.DataGenerateFactory;
import com.wing.facade.mock.MockValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class MockData {

    private final static Logger LOGGER = LoggerFactory.getLogger(MockData.class);
    private final DataGenerateFactory DATA_GENERATE_FACTORY;


    public MockData() {
        DATA_GENERATE_FACTORY = new DataGenerateFactory();
    }


    public Object mock(Type type) {
        return generateTree(type, null);
    }


    private Object generateTree(Type type, Field field) {
        LOGGER.info("遍历:{}", type.toString());
        if (type instanceof Class) {
            Class typeClass = (Class) type;
            //如果是基础对象，则生成实例，不是则继续递归
            Object mockValue = DATA_GENERATE_FACTORY.generateValue(typeClass, field);
            if (mockValue != null) {
                return mockValue;
            } else {
                return eachObjectTree(typeClass);
            }
        } else if (isListClass(type)) {
            List<Object> list = Lists.newArrayList();
            for (int i = 0; i < 3; i++) {
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
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            return List.class.isAssignableFrom((Class<?>) parameterizedType.getRawType());
        }
        return false;
    }

    /**
     * 遍历对象
     * 用get set方法生成对象
     */
    private Object eachObjectTree(Class clazz) {
        Object mappedObject = BeanUtils.instantiate(clazz);
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(clazz);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            if (propertyDescriptor.getWriteMethod() != null && propertyDescriptor.getReadMethod() != null) {
                String propertyName = propertyDescriptor.getName();
                try {
                    Field propertyField = clazz.getDeclaredField(propertyName);
                    Object instanceValue = generateTree(propertyDescriptor.getReadMethod().getGenericReturnType(), propertyField);
                    propertyDescriptor.getWriteMethod().invoke(mappedObject, instanceValue);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
                    LOGGER.error("字段设置值异常 fieldName:{},class:{}", propertyName, clazz.toString(), e);
                }
            }
        }
        return mappedObject;
    }





}
