package com.wing.core.mock.generate.handler;

import com.wing.core.mock.generate.AbstractGenerateHandler;

import java.util.Random;

public class StringGenerateHandler extends AbstractGenerateHandler {

    @Override
    public boolean canHandler(Class clazz) {
        return String.class.isAssignableFrom(clazz);
    }

    @Override
    public  String defaultValue(Class clazz) {
        return generateRandomString();
    }

    @Override
    public  Object mockInstance(String str, Class mockClass) {
        return str;
    }

    private String generateRandomString() {
        StringBuilder stringBuilder = new StringBuilder();
        String randomString = "我爱中国";
        for (int i = 0; i < 5; i++) {
            stringBuilder.append(randomString.toCharArray()[new Random().nextInt(randomString.toCharArray().length)]);
        }
        return stringBuilder.toString();
    }

}
