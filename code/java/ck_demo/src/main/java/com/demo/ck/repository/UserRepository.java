package com.demo.ck.repository;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.demo.ck.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

// jap mysql
public interface UserRepository extends JpaRepository<User, Integer> {
    @DS("ck")
    @Query("select count(*) from User")
    public Integer countAll();

    @Query(value = "select * from w2000 limit 1", nativeQuery = true)
    public User getOne();

}
