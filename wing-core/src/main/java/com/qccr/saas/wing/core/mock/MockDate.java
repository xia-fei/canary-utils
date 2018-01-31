package com.qccr.saas.wing.core.mock;

import com.google.common.collect.Lists;
import com.qccr.saas.wing.facade.mock.MockValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.NumberUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class MockDate {

    private final static Logger LOGGER = LoggerFactory.getLogger(MockDate.class);

    private int stringLength = 5;
    private int numberMaxValue = 100;
    private int listSize = 3;
    private String randomString = "我爱中国";
    private final Set<Class<?>> BASIC_NUMBER_TYPES;


    public MockDate() {
        Set<Class<?>> numberTypes = new HashSet<Class<?>>(5 + NumberUtils.STANDARD_NUMBER_TYPES.size());
        numberTypes.add(int.class);
        numberTypes.add(long.class);
        numberTypes.add(float.class);
        numberTypes.add(double.class);
        numberTypes.add(short.class);
        numberTypes.addAll(NumberUtils.STANDARD_NUMBER_TYPES);
        BASIC_NUMBER_TYPES = Collections.unmodifiableSet(numberTypes);
    }

    public void setListSize(int listSize) {
        this.listSize = listSize;
    }

    public void setRandomString(String randomString) {
        this.randomString = randomString;
    }


    public void setStringLength(int stringLength) {
        this.stringLength = stringLength;
    }


    public void setNumberMaxValue(int numberMaxValue) {
        this.numberMaxValue = numberMaxValue;
    }

    public Object mock(Type type) {
        return generateTree(type, null);
    }

    ;

    private Object generateTree(Type type, Field field) {
        if (Class.class.isInstance(type)) {
            Class typeClass = (Class) type;
            if (isSimpleType(typeClass)) {
                return generateValue(typeClass, field);
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

    private boolean isSimpleType(Class clazz) {
        return String.class.isAssignableFrom(clazz) || this.BASIC_NUMBER_TYPES.contains(clazz);
    }

    /**
     * 支持field is null
     */
    private Object generateValue(Class clazz, Field field) {
        MockValue mockValue = field.getAnnotation(MockValue.class);
        if (mockValue != null) {
            String randomValue = mockValue.value()[new Random().nextInt(mockValue.value().length)];
            if (String.class.isAssignableFrom(clazz)) {
                return randomValue;
            } else {
                return NumberUtils.parseNumber(randomValue, getNumberClass(clazz));
            }
        } else {
            return generateDefaultValue(clazz);
        }
    }

    private Class getNumberClass(Class numberClass) {
        if (int.class.isAssignableFrom(numberClass)) {
            return Integer.class;
        } else if (long.class.isAssignableFrom(numberClass)) {
            return Long.class;
        } else if (float.class.isAssignableFrom(numberClass)) {
            return Float.class;
        } else if (double.class.isAssignableFrom(numberClass)) {
            return Double.class;
        } else if (short.class.isAssignableFrom(numberClass)) {
            return Short.class;
        }
        return numberClass;

    }

    private Object generateDefaultValue(Class classType) {
        if (String.class.isAssignableFrom(classType)) {
            return generateRandomString();
        } else {
            return NumberUtils.parseNumber(String.valueOf(new Random().nextInt(this.numberMaxValue)), getNumberClass(classType));
        }
    }

    private String generateRandomString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < this.stringLength; i++) {
            stringBuilder.append(randomString.toCharArray()[new Random().nextInt(randomString.toCharArray().length)]);
        }
        return stringBuilder.toString();
    }

}
