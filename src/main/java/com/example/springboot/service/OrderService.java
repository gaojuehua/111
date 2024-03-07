package com.example.springboot.service;

import com.example.springboot.entity.Orders;


import java.util.List;
import java.util.Map;


public interface OrderService {
    List<Orders> list();

    Map<String, Object> getCountByTimeRange(String timeRange);
}