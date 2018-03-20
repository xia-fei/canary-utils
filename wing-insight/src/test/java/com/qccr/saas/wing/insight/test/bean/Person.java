package com.qccr.saas.wing.insight.test.bean;

import java.io.Serializable;
import java.util.Date;

public class Person implements Serializable{
    private String name;
    private Integer age;
    private Date birthday;
    private Integer sex;



    public Integer getSex() {
        return sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
