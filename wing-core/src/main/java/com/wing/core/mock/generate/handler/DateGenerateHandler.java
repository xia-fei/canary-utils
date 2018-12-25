package com.wing.core.mock.generate.handler;

import com.wing.core.mock.generate.AbstractGenerateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class DateGenerateHandler extends AbstractGenerateHandler {
    Logger LOGGER = LoggerFactory.getLogger(DateGenerateHandler.class);

    @Override
    public boolean canHandler(Class clazz) {
        return Date.class.isAssignableFrom(clazz) || java.sql.Date.class.isAssignableFrom(clazz);
    }

    @Override
    public Object mockInstance(String str, Class mockClass) {
        if (str != null) {
            LOGGER.warn("时间暂时不支持属性自定义");
        }
        if (Date.class.isAssignableFrom(mockClass)) {
            return new Date();
        } else {
            return new java.sql.Date(System.currentTimeMillis());
        }
    }

    @Override
    public String defaultValue(Class clazz) {
        return null;
    }

}
