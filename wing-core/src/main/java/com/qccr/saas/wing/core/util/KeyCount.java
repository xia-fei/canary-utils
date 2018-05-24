package com.qccr.saas.wing.core.util;

import com.google.common.base.MoreObjects;

import java.util.HashMap;
import java.util.Map;

public class KeyCount {
    private Map<String, Integer> countMap = new HashMap<String, Integer>();

    public void increment(String key) {
        Integer count = this.countMap.get(key);
        if (count == null) {
            this.countMap.put(key, 0);
        }
    }

    public int get(String key) {
        return MoreObjects.firstNonNull(countMap.get(key), 0);
    }
}
