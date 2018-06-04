package com.wing.mybatis.test;

import org.apache.ibatis.annotations.Param;

public interface Mapper {
    String test();
    int updateName(@Param("name") String name,@Param("id") int id);
    int addName(@Param("name")String name);

    int addSystemUser(String user,int id);
}
