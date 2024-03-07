package com.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot.controller.ILKPO;
import com.example.springboot.entity.Orders;
import com.example.springboot.service.OrdersService;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;


@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
    List<Orders> list();

//     static List<ILKPO> getCountByTimeRange(@Param("timeRange") String timeRange, @Param("type") int type){
//
//         return  OrdersMapper.getCountByTimeRange(timeRange,type);
//     };

}
