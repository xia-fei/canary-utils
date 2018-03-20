package com.qccr.saas.wing.insight.test.bean;

public class NestRO {
    private Dog dog;
    private CatRO catRO;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    public CatRO getCatRO() {
        return catRO;
    }

    public void setCatRO(CatRO catRO) {
        this.catRO = catRO;
    }
}
