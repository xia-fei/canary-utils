package org.xiafei.canary.mock.handler;


import org.xiafei.canary.mock.AbstractGenerateHandler;

import java.util.Random;

public class BooleanGenerateHandler extends AbstractGenerateHandler {

    @Override
    public boolean canHandler(Class clazz) {
        return Boolean.class.isAssignableFrom(clazz) || boolean.class.isAssignableFrom(clazz);
    }

    @Override
    public Object mockInstance(String str, Class mockClass) {
        return Boolean.valueOf(str);
    }

    @Override
    public Object defaultValue(Class clazz) {
        return new Random().nextBoolean();
    }


}
