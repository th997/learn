package com.demo.spring_boot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class User {
    private Long userId;
    private String name;
}
