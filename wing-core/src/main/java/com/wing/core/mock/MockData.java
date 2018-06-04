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
        if (Class.class.isInstance(type)) {
            Class typeClass = (Class) type;
            Object mockValue = DATA_GENERATE_FACTORY.generateValue(typeClass, getFieldValues(field));
            if (mockValue != null) {
                return mockValue;
            } else {
                return generateObject2(typeClass);
            }
        } else if (isListClass(type)) {
            List<Object> list = Lists.newArrayList();
            int listSize = 3;
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


    /**
     * 用反射方式生成对象
     * 使用 {@link #generateObject2(Class)}
     */
    @Deprecated
    private Object generateObject(Class clazz) {
        Object objMapper = BeanUtils.instantiate(clazz);
        for (Field field : clazz.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                if (field.get(objMapper) == null) {
                    field.set(objMapper, generateTree(field.getGenericType(), field));
                }
            } catch (IllegalAccessException e) {
                LOGGER.error("不能set的字段", e);
            }
        }
        return objMapper;
    }

    /**
     * 用get set方法生成对象
     */
    private Object generateObject2(Class clazz) {
        Object mappedObject = BeanUtils.instantiate(clazz);
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(clazz);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            if (propertyDescriptor.getWriteMethod() != null && propertyDescriptor.getReadMethod() != null) {
                String propertyName = propertyDescriptor.getName();
                Field propertyField = null;
                try {
                    propertyField = clazz.getDeclaredField(propertyName);
                } catch (NoSuchFieldException e) {
                    LOGGER.warn("获取Field失败", e);
                }
                try {
                    propertyDescriptor.getWriteMethod().invoke(mappedObject, generateTree(propertyDescriptor.getReadMethod().getGenericReturnType(), propertyField));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return mappedObject;
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
