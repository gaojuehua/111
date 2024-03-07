package com.example.springboot.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot.controller.ILKPO;
import com.example.springboot.entity.Inpantient;
import com.example.springboot.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InpantientMapper extends BaseMapper<Inpantient> {
    List<Inpantient> list();

    List<ILKPO> getCountByTimeRange(@Param("timeRange") String timeRange, @Param("type") int type);

}
