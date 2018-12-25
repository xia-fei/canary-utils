package com.wing.core.mock.generate.handler;

import com.wing.core.mock.generate.AbstractGenerateHandler;
import org.springframework.util.Assert;

import java.util.Random;

public class EnumGenerateHandler extends AbstractGenerateHandler {

    @Override
    public boolean canHandler(Class mockClass) {
        return Enum.class.isAssignableFrom(mockClass);
    }

    @Override
    public String defaultValue(Class clazz) {
        return null;
    }

    @Override
    public Object mockInstance(String str, Class mockClass) {
        Assert.isNull(str, "暂时不支持枚举类自定义赋值");
        Object[] enumConstants = mockClass.getEnumConstants();
        return enumConstants[new Random().nextInt(enumConstants.length)];
    }


}
