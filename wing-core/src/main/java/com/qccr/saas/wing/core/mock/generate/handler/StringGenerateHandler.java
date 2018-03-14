package com.qccr.saas.wing.core.mock.generate.handler;

import com.qccr.saas.wing.core.mock.generate.ClassGenerateHandler;

import java.util.Random;

public class StringGenerateHandler extends AbstractGenerateHandler implements ClassGenerateHandler {

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
        String randomString = "汽车超人";
        for (int i = 0; i < 5; i++) {
            stringBuilder.append(randomString.toCharArray()[new Random().nextInt(randomString.toCharArray().length)]);
        }
        return stringBuilder.toString();
    }

}
