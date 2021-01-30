package com.demo.spring_boot.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Order {
    private Long orderId;
    private String detail;
}
