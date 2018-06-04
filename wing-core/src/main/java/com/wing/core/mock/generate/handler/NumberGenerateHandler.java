package com.wing.core.mock.generate.handler;

import com.wing.core.mock.generate.ClassGenerateHandler;
import org.springframework.util.NumberUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class NumberGenerateHandler extends AbstractGenerateHandler implements ClassGenerateHandler {


    private final Set<Class<?>> BASIC_NUMBER_TYPES;

    public NumberGenerateHandler() {
        Set<Class<?>> numberTypes = new HashSet<Class<?>>(5 + NumberUtils.STANDARD_NUMBER_TYPES.size());
        numberTypes.add(int.class);
        numberTypes.add(long.class);
        numberTypes.add(float.class);
        numberTypes.add(double.class);
        numberTypes.add(short.class);
        numberTypes.addAll(NumberUtils.STANDARD_NUMBER_TYPES);
        BASIC_NUMBER_TYPES = Collections.unmodifiableSet(numberTypes);
    }

    @Override
    public boolean canHandler(Class clazz) {
        return BASIC_NUMBER_TYPES.contains(clazz);
    }

    @Override
    public Object generateValue(Class valueClass, String[] mockValues) {
        String value = randomValue(mockValues, new String[]{randomNumberText(isDecimals(valueClass))});
        return NumberUtils.parseNumber(value, getNumberClass(valueClass));
    }

    private boolean isDecimals(Class valueClass){
        return Double.class.isAssignableFrom(valueClass)
                || Float.class.isAssignableFrom(valueClass)
                ||float.class.isAssignableFrom(valueClass)
                ||double.class.isAssignableFrom(valueClass);
    }

    private String randomNumberText(boolean isDecimals) {
        if(isDecimals){
            return String.valueOf(new Random().nextInt(1000)/100D);
        }else {
            return String.valueOf(new Random().nextInt(1000));
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


}
