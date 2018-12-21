package com.wing.insight.test.bean;

import java.io.Serializable;

public class CatRO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private Person host;

    public Person getHost() {
        return host;
    }

    public void setHost(Person host) {
        this.host = host;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
