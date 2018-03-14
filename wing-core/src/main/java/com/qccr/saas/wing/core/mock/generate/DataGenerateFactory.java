package com.qccr.saas.wing.core.mock.generate;

import com.qccr.saas.wing.core.mock.generate.handler.BooleanGenerateHandler;
import com.qccr.saas.wing.core.mock.generate.handler.DateGenerateHandler;
import com.qccr.saas.wing.core.mock.generate.handler.NumberGenerateHandler;
import com.qccr.saas.wing.core.mock.generate.handler.StringGenerateHandler;

import java.util.ArrayList;
import java.util.List;

public class DataGenerateFactory {
    private final List<ClassGenerateHandler> classGenerateHandlerList = new ArrayList<>();

    public DataGenerateFactory() {
        classGenerateHandlerList.add(new BooleanGenerateHandler());
        classGenerateHandlerList.add(new DateGenerateHandler());
        classGenerateHandlerList.add(new NumberGenerateHandler());
        classGenerateHandlerList.add(new StringGenerateHandler());
    }

    /**
     * @param valueClass 基础对象的class
     * @param mockValue  生成的值
     * @return 如果不是基础对象，则不生成值
     */
    public Object generateValue(Class valueClass, String[] mockValue) {
        for (ClassGenerateHandler classGenerateHandler : classGenerateHandlerList) {
            if (classGenerateHandler.canHandler(valueClass)) {
                return classGenerateHandler.generateValue(valueClass, mockValue);
            }
        }
        return null;
    }


}
