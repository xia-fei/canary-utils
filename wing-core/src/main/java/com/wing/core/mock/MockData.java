package com.wing.core.mock;

import ch.qos.logback.core.pattern.parser.Node;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class MockData {

    private final static Logger LOGGER = LoggerFactory.getLogger(MockData.class);
    private final DataGenerateFactory DATA_GENERATE_FACTORY;


    public MockData() {
        DATA_GENERATE_FACTORY = new DataGenerateFactory();
    }


    public Object mock(Type type) {
        ObjectPath root = new ObjectPath(null, (Class) type);
        return generateTree(type, null, root);
    }



    private Object generateTree(Type type, Field field, ObjectPath objectPath) {
        if(isOverDepth(objectPath)){
            return null;
        }
        if (type instanceof Class) {
            Class typeClass = (Class) type;
            //如果是基础对象，则生成实例，不是则继续递归
            Object mockValue = DATA_GENERATE_FACTORY.generateValue(typeClass, field);
            if (mockValue != null) {
                return mockValue;
            } else {
                return eachObjectTree(typeClass, objectPath);
            }
        } else if (isListClass(type)) {
            List<Object> list = Lists.newArrayList();
            for (int i = 0; i < 3; i++) {
                list.add(generateTree(getListClass(type), field, objectPath));
            }
            return list;
        }
        throw new IllegalArgumentException("未能转换的类型:" + String.valueOf(type));
    }



    private boolean isOverDepth(ObjectPath objectPath) {

        final LoadingCache<String, Integer> classCount = CacheBuilder.newBuilder().build(new CacheLoader<String, Integer>() {
            @Override
            public Integer load(String key) throws Exception {
                return 1;
            }
        });
        ObjectPath prev = objectPath.prev;
        while (prev != null) {
            String className = prev.item.getName();
            if (classCount.getUnchecked(className) > 5) {
                return true;
            }
            classCount.put(className, classCount.getUnchecked(className) + 1);
            prev = prev.prev;
        }

        return false;
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
    private Object eachObjectTree(Class clazz, ObjectPath objectPath) {

        Object mappedObject = BeanUtils.instantiate(clazz);
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(clazz);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            if (propertyDescriptor.getWriteMethod() != null && propertyDescriptor.getReadMethod() != null) {
                String propertyName = propertyDescriptor.getName();
                try {
                    Field propertyField = clazz.getDeclaredField(propertyName);
                    //产生新的节点
                    ObjectPath currentPath = new ObjectPath(objectPath, propertyField.getType());
                    Object instanceValue = generateTree(propertyDescriptor.getReadMethod().getGenericReturnType(), propertyField, currentPath);
                    propertyDescriptor.getWriteMethod().invoke(mappedObject, instanceValue);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
                    LOGGER.error("字段设置值异常 fieldName:{},class:{}", propertyName, clazz.toString(), e);
                }
            }
        }
        return mappedObject;
    }

    /**
     * 遍历路径单向链表
     */
    private class ObjectPath {
        Class item;
        MockData.ObjectPath prev;

        ObjectPath(MockData.ObjectPath prev, Class element) {
            this.item = element;
            this.prev = prev;
        }
    }


}
