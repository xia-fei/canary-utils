package com.wing.core.mock.generate.handler;

import com.wing.core.mock.generate.ClassGenerateHandler;

public class BooleanGenerateHandler extends AbstractGenerateHandler implements ClassGenerateHandler {
    private final String DEFAULT_VALUE[] = {"true", "false"};

    @Override
    public boolean canHandler(Class clazz) {
        return Boolean.class.isAssignableFrom(clazz) || boolean.class.isAssignableFrom(clazz);
    }

    @Override
    public Object generateValue(Class valueClass, String[] mockValues) {
        return Boolean.valueOf(randomValue(mockValues,DEFAULT_VALUE));
    }
}
