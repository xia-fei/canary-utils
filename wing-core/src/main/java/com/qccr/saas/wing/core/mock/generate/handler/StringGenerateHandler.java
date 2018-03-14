package com.qccr.saas.wing.core.mock.generate.handler;

import com.qccr.saas.wing.core.mock.generate.ClassGenerateHandler;

import java.util.Random;

public class StringGenerateHandler extends AbstractGenerateHandler implements ClassGenerateHandler {
    private String randomString = "我爱中国";

    @Override
    public boolean canHandler(Class clazz) {
        return String.class.isAssignableFrom(clazz);
    }

    @Override
    public Object generateValue(Class valueClass, String[] mockValues) {
        return randomValue(mockValues, new String[]{generateRandomString()});
    }

    private String generateRandomString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            stringBuilder.append(randomString.toCharArray()[new Random().nextInt(randomString.toCharArray().length)]);
        }
        return stringBuilder.toString();
    }

}
