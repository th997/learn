package com.demo.es.repository;

import com.demo.es.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

// mongodb
public interface UserMRepository extends MongoRepository<User, Integer> {
}