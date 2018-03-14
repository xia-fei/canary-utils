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

public class MockData {

    private final static Logger LOGGER = LoggerFactory.getLogger(MockData.class);

    private int stringLength = 5;
    private int numberMaxValue = 100;
    private int listSize = 3;
    private String randomString = "我爱中国";
    private final Set<Class<?>> BASIC_NUMBER_TYPES;


    public MockData() {
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
            if (canGenerateValue(typeClass)) {
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
    private boolean canGenerateValue(Class clazz){
        return isSimpleType(clazz)||isDateType(clazz);
    }

    private boolean isDateType(Class clazz){
        return Date.class.isAssignableFrom(clazz)|| java.sql.Date.class.isAssignableFrom(clazz);
    }

    /**
     * 支持field is null
     */
    private Object generateValue(Class valueClass, Field field) {
        MockValue mockValue = field.getAnnotation(MockValue.class);
        String[] mockValues = null;
        if (mockValue != null) {
            mockValues = mockValue.value();
        }
        if (String.class.isAssignableFrom(valueClass)) {
            return generateStringValue(mockValues);
        } else if(isDateType(valueClass)){
            return generateDateValue(valueClass);
        }else {
            return generateNumberValue(valueClass, mockValues);
        }
    }
    private Object generateDateValue(Class valueClass){
        if(Date.class.isAssignableFrom(valueClass)){
            return new Date();
        }else if(java.sql.Date.class.isAssignableFrom(valueClass)){
            return new java.sql.Date(System.currentTimeMillis());
        }else {
            throw new RuntimeException("不认识的Date类型"+valueClass.toString());
        }
    }



    /**
     * 生成字符串
     *
     * @param values mock的值
     * @return 生成的字符串
     */
    private Number generateNumberValue(Class numberClass, String[] values) {
        String numberValue;
        if (values != null) {
            numberValue = fetchRandomIndexString(values);
        } else {
            numberValue = fetchRandomNumberString();
        }
        return NumberUtils.parseNumber(numberValue, getNumberClass(numberClass));
    }


    /**
     * 生成字符串,values为空生成默认值
     *
     * @param values mock的值
     * @return 生成的字符串
     */
    private String generateStringValue(String[] values) {
        if (values != null) {
            return fetchRandomIndexString(values);
        } else {
            return generateRandomString();
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


    private String fetchRandomNumberString() {
        return String.valueOf(new Random().nextInt(this.numberMaxValue));
    }

    private String fetchRandomIndexString(String[] values) {
        return values[new Random().nextInt(values.length)];
    }


    /**
     * 根据randomString生成随机字符串
     *
     * @return 随机字符串
     */
    private String generateRandomString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < this.stringLength; i++) {
            stringBuilder.append(randomString.toCharArray()[new Random().nextInt(randomString.toCharArray().length)]);
        }
        return stringBuilder.toString();
    }
}
