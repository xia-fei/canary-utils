package com.wing.core.mock.generate;

public interface ClassGenerateHandler {
    public boolean canHandler(Class clazz);
    public Object generateValue(Class valueClass,String[] mockValues);
}
