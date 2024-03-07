package com.example.springboot.entity;


import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class hanjian {
    @TableId(type = IdType.AUTO)
    @Alias("序号")
    private Integer id;
    private String name;
    private String idcard;
    private String sex;
    private String age;
    private String phone;
    private String email;
    private String sort;
    private String ilk;
    private Integer authorid;
    private String time;
    private String content;
    private BigDecimal category;

    @TableField(exist = false)   //数据库无该字段
    private String user;
}