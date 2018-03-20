package com.qccr.saas.wing.core.mock.generate.handler;

import com.qccr.saas.wing.core.mock.generate.ClassGenerateHandler;

import java.util.Date;

public class DateGenerateHandler extends AbstractGenerateHandler implements ClassGenerateHandler {
    @Override
    public boolean canHandler(Class clazz) {
        return Date.class.isAssignableFrom(clazz)|| java.sql.Date.class.isAssignableFrom(clazz);
    }

    @Override
    public Object generateValue(Class valueClass, String[] mockValues) {
        if(Date.class.isAssignableFrom(valueClass)){
            return new Date();
        }else {
            return new java.sql.Date(System.currentTimeMillis());
        }
    }
}
