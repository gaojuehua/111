package com.example.springboot.entity;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Doctor {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @Alias("头像")
    private String avatar;
    @Alias("用户名")
    private String username;
    @Alias("姓名")
    private String name;
    @Alias("简介")
    private String description;
    @Alias("电话")
    private String phone;
    @Alias("邮件")
    private String email;
    @Alias("科室")
    private String sort;
    @Alias("排班")
    private String schedule;
    @Alias("入职时间")
    private String time;
    @Alias("职位")
    private String post;
    @Alias("是否公开")
    private Boolean open;
    @Alias("是否公开")
    private String states;




    @TableField(exist = false)
    private String user;
    @TableField(exist = false)
    private String userid;

}