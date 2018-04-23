package com.qccr.saas.wing.mybatis.test;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface BlogMapper {
  @Select("SELECT a from s_a")
  Integer selectBlog(Integer id);


  @Select("SELECT '${userId}'")
  String selectValue( String a, @Param("b") String b);


  @Select("SELECT '${param.isReward}'")
  String selectBoole(@Param("param") Parameters param);
}