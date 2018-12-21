package com.wing.core.mock.generate.handler;

public class EnumGenerateHandler extends AbstractGenerateHandler {

    @Override
    public boolean canHandler(Class mockClass) {
        return Enum.class.isAssignableFrom(mockClass);
    }

    @Override
    String defaultValue(Class clazz) {
        return null;
    }

    @Override
    Object mockInstance(String str, Class mockClass) {
        return null;
    }

    public static void main(String[] args) {
       Color red=Color.Red;
       System.out.println();
    }
}

enum Color{
    Red,Yellow,Green
}
