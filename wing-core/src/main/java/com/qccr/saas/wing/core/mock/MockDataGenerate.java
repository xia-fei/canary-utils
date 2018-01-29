package com.qccr.saas.wing.core.mock;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.qccr.saas.wing.facade.mock.MockValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 调用
 * new MockDataGenerate().generateMock(YouRO.class);返回对应有数据的实体
 * 参考
 *  com.qccr.shprod.biz.view.ChartViewGenerate
 */
public class MockDataGenerate {
    private final Logger LOGGER = LoggerFactory.getLogger(MockDataGenerate.class);
    private int listSize = 3;
    private int intMax = 100;
    private final List<String> stringAll = ImmutableList.of("洗车", "保养", "汽车美容", "维修");
    private final ImmutableSet basicType = ImmutableSet.of("int", "double", "float", "long", "boolean");


    public void setListSize(int listSize) {
        this.listSize = listSize;
    }

    public void setIntMax(int intMax) {
        this.intMax = intMax;
    }

    public Object generateMock(Class<?> clazz) {
        return treeClass(clazz, null);
    }


    /**
     * 递归方法,返回class实例
     */
    private Object treeClass(Class<?> clazz, Field fieldObject) {
        if (isBasicType(clazz)) {
            return generateValueByField(clazz, fieldObject);
        }
        Object instance = newInstance(clazz);
        for (Field field : clazz.getDeclaredFields()) {
            if (isSkip(field)) {
                continue;
            }
            Type type = field.getGenericType();
            //普通对象
            Object value = null;
            if (Class.class.isInstance(type)) {
                value = treeClass(field.getType(), field);
                //List泛型对象
            } else if (ParameterizedType.class.isInstance(type)) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Class actualClass = (Class) parameterizedType.getActualTypeArguments()[0];
                List<Object> list = new ArrayList<>();
                for (int i = 0; i < listSize; i++) {
                    list.add(treeClass(actualClass, field));
                }
                value = list;
            } else {
                LOGGER.warn("未能解析的对象{}", type);
            }
            setFieldValue(field, instance, value);

        }
        return instance;
    }

    private boolean isSkip(Field field) {
        return field.getName().equals("serialVersionUID");
    }

    private boolean isBasicType(Class clazz) {
        String simpleName = clazz.getName();
        return simpleName.startsWith("java") || basicType.contains(simpleName);
    }

    private void setFieldValue(Field field, Object instance, Object value) {
        try {
            field.setAccessible(true);
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            LOGGER.error("设置属性失败{}", field, e);
        }
    }

    private Object newInstance(Class<?> empty) {
        try {
            return empty.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error("实例化失败{}", empty, e);
            Throwables.propagate(e);
        }
        return null;
    }




    /***
     * 生成此Empty里面的实例
     */
    private Object generateValueByField(Class<?> empty, Field field) {
        MockValue mockValueAnnotation = field.getAnnotation(MockValue.class);
        if (mockValueAnnotation != null) {
            return createMockData(mockValueAnnotation.value(), empty);
        }
        return generateDefaultValue(empty);
    }


    private Object createMockData(String[] values, Class<?> dataClass) {
        String value = values[new Random().nextInt(values.length)];
        if (dataClass.isAssignableFrom(Integer.class)) {
            return Integer.parseInt(value);
        } else if (dataClass.isAssignableFrom(String.class)) {
            return value;
        } else if (dataClass.isAssignableFrom(Double.class)) {
            return Double.parseDouble(value);
        } else {
            LOGGER.warn("未识别的类型{}", dataClass);
            return null;
        }
    }

    /**
     * 根据class生成基础对象
     */
    private Object generateDefaultValue(Class<?> valueType) {
        String className = valueType.getName();
        Object returnValue;
        if (String.class.isAssignableFrom(valueType)) {
            returnValue = generateString();
        } else if (Integer.class.isAssignableFrom(valueType) || className.equals("int")) {
            returnValue = generateInt();
        } else if (Double.class.isAssignableFrom(valueType) || className.equals("double")) {
            returnValue = generateInt() / 10.0D;
        } else if (Float.class.isAssignableFrom(valueType) || className.equals("float")) {
            returnValue = (float) (generateInt() / 10.0);
        } else if (Long.class.isAssignableFrom(valueType) || className.equals("long")) {
            returnValue = (long) generateInt();
        } else if (Date.class.isAssignableFrom(valueType)) {
            returnValue = new Date();
        } else if (Boolean.class.isAssignableFrom(valueType) || className.equals("boolean")) {
            returnValue = true;
        } else {
            returnValue = null;
            LOGGER.warn("未能生成基本数据对象,{}", valueType);
        }
        return returnValue;
    }

    private String generateString() {
        return stringAll.get(new Random().nextInt(stringAll.size()));
    }

    private int generateInt() {
        return new Random().nextInt(intMax);
    }
}