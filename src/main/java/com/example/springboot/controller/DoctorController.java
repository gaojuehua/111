package com.example.springboot.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.HoneyLogs;
import com.example.springboot.common.LogType;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Doctor;
import com.example.springboot.entity.User;
import com.example.springboot.service.DoctorService;
import com.example.springboot.service.UserService;
import com.example.springboot.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Resource
    DoctorService doctorService;
    @Resource
    UserService userService;

    /**
     * 新增信息
     */
    @HoneyLogs(operation = "医生信息" , type = LogType.ADD)
    @PostMapping("/add")
    public Result add(@RequestBody Doctor doctor) {
        User currentUser = TokenUtils.getCurrentUser();  //获取当前登录的用户信息
        doctor.setUserid(String.valueOf(currentUser.getId()));
        doctor.setTime(DateUtil.today());     //  2023-11-30
        doctorService.save(doctor);
        return Result.success();
    }

    /**
     * 修改信息
     */
    @HoneyLogs(operation = "医生信息", type = LogType.UPDATE)
    @PutMapping("/update")
    public Result update(@RequestBody Doctor doctor) {
        doctorService.updateById(doctor);
        return Result.success();
    }

    /**
     * 删除信息
     */
    @HoneyLogs(operation = "医生信息",type = LogType.DELETE)
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        doctorService.removeById(id);
        return Result.success();
    }

    /**
     * 批量删除信息
     */
    @HoneyLogs(operation = "医生信息", type = LogType.BATCH_DELETE)
    @DeleteMapping("/delete/batch")
    public Result batchdelete(@RequestBody List<Integer> ids) {
        doctorService.removeBatchByIds(ids);
        return Result.success();
    }

    /**
     * 查询用户信息
     */
    @GetMapping("/selectall")
    public Result selectAll() {
        List<Doctor> ordersList = doctorService.list(new QueryWrapper<Doctor>().orderByDesc("id"));
        return Result.success(ordersList);
    }

    /**
     * 用户查询公告
     * */
    @GetMapping("/selectUserData")
    public Result selectUserData() {
        QueryWrapper<Doctor> queryWrapper = new QueryWrapper<Doctor>().orderByDesc("id");
        queryWrapper.eq("open",1);   //用户只能看到公开的公告数据
        List<Doctor> userList = doctorService.list(queryWrapper);
        return Result.success(userList);
    }

    /**
     * 根据id查询用户信息
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Doctor doctor = doctorService.getById(id);
        User user = userService.getById(doctor.getId());
        if(user != null){
            doctor.setUser(user.getName());
        }
        return Result.success(doctor);
    }

    /**
     * 分页模糊查询
     * pageNum当前页码
     */
    @GetMapping("/selectByMoPage")
    public Result selectByMoPage(@RequestParam Integer pageNum,
                                 @RequestParam Integer pageSize,
                                 @RequestParam String name) {
        QueryWrapper<Doctor> queryWrapper = new QueryWrapper<Doctor>().orderByDesc("id");    //默认倒叙，让最新的数据在最上面，需要正序的话删除.orderByDesc("id")
        queryWrapper.like(StrUtil.isNotBlank(name),"name",name);
        Page<Doctor> page = doctorService.page(new Page<>(pageNum, pageSize), queryWrapper);
        List<Doctor> records = page.getRecords();
        for(Doctor record : records){
            Integer authorid = record.getId();
            User user = userService.getById(authorid);
            if(user != null){
                record.setUser(user.getName());
            }
        }
        return Result.success(page);
    }

    @GetMapping("/selectByMoPage0")
    public Result selectByMoPage0(@RequestParam Integer pageNum,
                                 @RequestParam Integer pageSize,
                                 @RequestParam String name,
                                 @RequestParam String sort) {
        QueryWrapper<Doctor> wrapper = new QueryWrapper<Doctor>().orderByDesc("id");    //默认倒叙，让最新的数据在最上面，需要正序的话删除.orderByDesc("id")
        wrapper.eq("open",1)
                .and(qw -> qw.like("name",name).or().like("sort",sort));
        wrapper.like(StrUtil.isNotBlank(name),"name",name);
        wrapper.like(StrUtil.isNotBlank(sort),"sort",sort);
        Page<Doctor> page = doctorService.page(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(page);
    }

    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum,
                                  @RequestParam Integer pageSize,
                                  @RequestParam String name,
                                  @RequestParam String sort,
                                   @RequestParam String schedule) {
        QueryWrapper<Doctor> wrapper = new QueryWrapper<Doctor>().orderByDesc("id");
        wrapper.eq("open",1)
                        .and(qw -> qw.like("name",name).or().like("sort",sort).or().like("schedule",schedule));
        wrapper.like(StrUtil.isNotBlank(name),"name",name);
        wrapper.like(StrUtil.isNotBlank(sort),"sort",sort);
        wrapper.like(StrUtil.isNotBlank(schedule),"schedule",schedule);
        Page<Doctor> page = doctorService.page(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(page);
    }
}