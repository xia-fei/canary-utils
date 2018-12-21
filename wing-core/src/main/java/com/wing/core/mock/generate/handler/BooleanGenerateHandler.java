package com.wing.core.mock.generate.handler;

public class BooleanGenerateHandler extends AbstractGenerateHandler {
    private final String DEFAULT_VALUE[] = {"true", "false"};

    @Override
    public boolean canHandler(Class clazz) {
        return Boolean.class.isAssignableFrom(clazz) || boolean.class.isAssignableFrom(clazz);
    }

    @Override
    Object mockInstance(String str, Class mockClass) {
        return Boolean.valueOf(str);
    }

    @Override
    String defaultValue(Class clazz) {
        return getRandom(DEFAULT_VALUE);
    }


}
