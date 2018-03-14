package com.qccr.saas.wing.core.mock.generate.handler;

import java.util.Random;

abstract class AbstractGenerateHandler {
    private String randomValue(String[] values){
        return values[new Random().nextInt(values.length)];
    }

    String randomValue(String[] firstValue, String[] secondValue){
        if(firstValue!=null){
            return randomValue(firstValue);
        }else {
            return randomValue(secondValue);
        }
    }
}
