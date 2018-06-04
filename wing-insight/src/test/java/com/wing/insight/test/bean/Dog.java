package com.wing.insight.test.bean;


import com.qccr.saas.wing.facade.mock.MockValue;

import java.util.List;

public class Dog {
    @MockValue({"小黑","小黄","小白"})
    private List<String> friends;
    @MockValue({"张三","李四"})
    private String name;
    private Double age;


    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAge() {
        return age;
    }

    public void setAge(double age) {
        this.age = age;
    }
}
