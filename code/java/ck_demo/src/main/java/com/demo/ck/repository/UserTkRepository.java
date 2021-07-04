package com.demo.ck.repository;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.demo.ck.entity.User;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

// tk.mybatis mysql
public interface UserTkRepository extends Mapper<User>, IdsMapper<User>, MySqlMapper<User> {

    @DS("ck")
    @Select("select count(*) from w2000")
    public Integer countAll();

    @Select(value = "select * from w2000 limit 1")
    public User getOne();

}
