package org.xiafei.canary.mock;


import org.xiafei.canary.mock.handler.*;
import org.xiafei.mock.MockValue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DataGenerateFactory {
    private final List<AbstractGenerateHandler> classGenerateHandlerList = new ArrayList<>();

    public DataGenerateFactory(MockSettings settings) {
        classGenerateHandlerList.add(new BooleanGenerateHandler());
        classGenerateHandlerList.add(new DateGenerateHandler());
        classGenerateHandlerList.add(new NumberGenerateHandler());
        classGenerateHandlerList.add(new StringGenerateHandler(settings));
        classGenerateHandlerList.add(new EnumGenerateHandler());
    }

    /**
     * @param valueClass 基础对象的class
     * @return 如果不是基础对象，则不生成值
     */
    public Object generateValue(Class valueClass, Field field) {
        for (AbstractGenerateHandler classGenerateHandler : classGenerateHandlerList) {
            if (classGenerateHandler.canHandler(valueClass)) {
                return classGenerateHandler.generateValue(valueClass, getFieldValues(field));
            }
        }
        return null;
    }

    private String[] getFieldValues(Field field) {
        if (field != null) {
            MockValue mockValue = field.getAnnotation(MockValue.class);
            if (mockValue != null) {
                return mockValue.value();
            }
        }
        return null;

    }


}
