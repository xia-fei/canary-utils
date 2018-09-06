package com.wing.excel.text;

import com.wing.excel.BeanHandler;
import com.wing.excel.ExcelUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author 夏飞
 */
public class Main {

    @Test
    public void test1() throws FileNotFoundException {
        List<Cat> list=new ArrayList<>();
        list.add(new Cat("111",1));
        list.add(new Cat("222",2));
        list.add(new Cat("333",3));
        list.add(new Cat("444",4));
        list.add(new Cat("555",5));
        LinkedHashMap<String,String> header=new LinkedHashMap<>();
        header.put("age","猫年龄");
        header.put("name","猫名称");
        ExcelUtils.export(list, new BeanHandler<Cat>() {
                    @Override
                    public void handler(Cat cat) {
                        cat.setAge(cat.getAge()+100);
                    }
        }, header,new FileOutputStream(new File("/1.xlsx")));

    }

}

class Cat{
    private String name;
    private Integer age;

    public Cat(String name, Integer age) {
        this.name = name;
        this.age = age;
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
