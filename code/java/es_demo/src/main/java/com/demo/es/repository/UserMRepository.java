package com.demo.es.repository;

import com.demo.es.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

// mongodb
public interface UserMRepository extends MongoRepository<User, Integer> {
    public List<User> findByC1(String c1);
}