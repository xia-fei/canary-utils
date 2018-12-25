package com.wing.core.mock.generate.handler;

import com.wing.core.mock.generate.AbstractGenerateHandler;

public class BooleanGenerateHandler extends AbstractGenerateHandler {
    private final String DEFAULT_VALUE[] = {"true", "false"};

    @Override
    public boolean canHandler(Class clazz) {
        return Boolean.class.isAssignableFrom(clazz) || boolean.class.isAssignableFrom(clazz);
    }

    @Override
    public Object mockInstance(String str, Class mockClass) {
        return Boolean.valueOf(str);
    }

    @Override
    public String defaultValue(Class clazz) {
        return getRandom(DEFAULT_VALUE);
    }


}
