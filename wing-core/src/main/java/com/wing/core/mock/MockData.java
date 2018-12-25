package com.wing.core.mock;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.wing.core.mock.generate.DataGenerateFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class MockData {

    private final static Logger LOGGER = LoggerFactory.getLogger(MockData.class);
    private final DataGenerateFactory DATA_GENERATE_FACTORY;
    private final CacheLoader<String, Integer> COUNTER_LOADER = new CacheLoader<String, Integer>() {
        @Override
        public Integer load(String key) throws Exception {
            return 1;
        }
    };

    public MockData() {
        DATA_GENERATE_FACTORY = new DataGenerateFactory();
    }


    /**
     * mock入口
     */
    public Object mock(Type type) {
        ObjectPath root = new ObjectPath(null, (Class) type);
        return createAllObject(type, null, root);
    }


    private Object createAllObject(Type type, Field field, ObjectPath objectPath) {
        if (isOverDepth(objectPath)) {
            return null;
        }
        if (type instanceof Class) {
            Class typeClass = (Class) type;
            //如果是基础对象，则生成实例，不是则继续递归
            Object mockValue = DATA_GENERATE_FACTORY.generateValue(typeClass, field);
            if (mockValue != null) {
                return mockValue;
            } else {
                return createStandardObject(typeClass, objectPath);
            }
        } else if (isCollection(type)) {
            return createCollectionObject((ParameterizedType) type, field, objectPath);
        }
        throw new IllegalArgumentException("未能转换的类型:" + String.valueOf(type));
    }

    private boolean isCollection(Type type) {
        ParameterizedType parameterizedType = (ParameterizedType) type;
        return Collection.class.isAssignableFrom((Class<?>) parameterizedType.getRawType());
    }

    /**
     * 判断递归调用
     */
    private boolean isOverDepth(ObjectPath objectPath) {
        LoadingCache<String, Integer> classCount = CacheBuilder.newBuilder().build(COUNTER_LOADER);
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


    @SuppressWarnings("unchecked")
    private Object createCollectionObject(ParameterizedType parameterizedType, Field field, ObjectPath objectPath) {
        if (parameterizedType.getRawType() instanceof Class) {
            Class collectionClass = (Class) parameterizedType.getRawType();
            Collection collection = (Collection) createCollectionInstance(collectionClass);
            collection.add(createAllObject(parameterizedType.getActualTypeArguments()[0], field, objectPath));
            collection.add(createAllObject(parameterizedType.getActualTypeArguments()[0], field, objectPath));
            collection.add(createAllObject(parameterizedType.getActualTypeArguments()[0], field, objectPath));
            return collection;
        }
        throw new RuntimeException("类型不支持");


    }

    private Object createCollectionInstance(Class collectionClass) {
        if (collectionClass.isInterface()) {
            if (List.class.isAssignableFrom(collectionClass)) {
                return new ArrayList<>();
            } else if (Set.class.isAssignableFrom(collectionClass)) {
                return new HashSet<>();
            }
            throw new RuntimeException("不支持的集合类型" + collectionClass.toString());
        } else {
            return collectionClass.isInterface();
        }

    }


    /**
     * 遍历对象
     * 用get set方法生成对象
     */
    private Object createStandardObject(Class clazz, ObjectPath objectPath) {
        Object mappedObject = BeanUtils.instantiate(clazz);
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(clazz);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            if (propertyDescriptor.getWriteMethod() != null && propertyDescriptor.getReadMethod() != null) {
                String propertyName = propertyDescriptor.getName();
                try {
                    Field propertyField = clazz.getDeclaredField(propertyName);
                    //产生新的节点
                    ObjectPath currentPath = new ObjectPath(objectPath, propertyField.getType());
                    Object instanceValue = createAllObject(propertyDescriptor.getReadMethod().getGenericReturnType(), propertyField, currentPath);
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
