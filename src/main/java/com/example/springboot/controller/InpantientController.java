package com.example.springboot.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.HoneyLogs;
import com.example.springboot.common.LogType;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Inpantient;
import com.example.springboot.entity.User;
import com.example.springboot.service.*;
import com.example.springboot.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/inpantient")
public class InpantientController {

    @Autowired
    InpantientService inpantientService;
    @Autowired
    UserService userService;
    @Autowired
    DoctorService doctorService;

    /**
     * 新增信息
     */
    @HoneyLogs(operation = "病历表" , type = LogType.ADD)
    @PostMapping("/add")
    public Result add(@RequestBody Inpantient inpantient) {
        User currentUser = TokenUtils.getCurrentUser();  //获取当前登录的用户信息
        inpantient.setSort(currentUser.getSort());
        inpantient.setAuthorid(currentUser.getId());
        inpantient.setTime(DateUtil.today());     //  2023-11-30
        inpantient.setCategory(BigDecimal.valueOf(1));
        inpantientService.save(inpantient);
        return Result.success();
    }


    /**
     * 修改信息
     */
    @HoneyLogs(operation = "病历表", type = LogType.UPDATE)
    @PutMapping("/update")
    public Result update(@RequestBody Inpantient inpantient) {
        inpantientService.updateById(inpantient);
        return Result.success();
    }

    /**
     * 删除信息
     */
    @HoneyLogs(operation = "病历表",type = LogType.DELETE)
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        User currentUser = TokenUtils.getCurrentUser();
        Inpantient inpantient = inpantientService.getById(id);
        if(!currentUser.getSort().equals(inpantient.getSort())){
            return Result.error("权限不足");
        }
        inpantientService.removeById(inpantient);
        return Result.success();
    }

    /**
     * 批量删除信息
     */
    @HoneyLogs(operation = "病历表", type = LogType.BATCH_DELETE)
    @DeleteMapping("/delete/batch")
    public Result batchdelete(@RequestBody List<Integer> ids,@PathVariable Integer id) {
        User currentUser = TokenUtils.getCurrentUser();
        Inpantient inpantient = inpantientService.getById(id);
        if(!currentUser.getSort().equals(inpantient.getSort())){
            return Result.error("权限不足");
        }
        inpantientService.removeBatchByIds(ids);
        return Result.success();
    }

    /**
     * 查询用户信息
     */
    @GetMapping("/selectall")
    public Result selectAll() {
        List<Inpantient> ordersList = inpantientService.list(new QueryWrapper<Inpantient>().orderByDesc("id"));
        return Result.success(ordersList);
    }

    /**
     * 查询用户公告
     * */
    @GetMapping("/selectUserData")
    public Result selectUserData() {
        QueryWrapper<Inpantient> queryWrapper = new QueryWrapper<Inpantient>().orderByDesc("id");
        queryWrapper.eq("open",1);   //用户只能看到公开的公告数据
        List<Inpantient> userList = inpantientService.list(queryWrapper);
        return Result.success(userList);
    }

    /**
     * 根据id查询用户信息
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Inpantient inpantient = inpantientService.getById(id);
        User user = userService.getById(inpantient.getAuthorid());
        if(user != null){
            inpantient.setUser(user.getName());
        }
        return Result.success(inpantient);
    }

    /**
     * 分页模糊查询
     * pageNum当前页码
     */
    @GetMapping("/selectByMoPage")
    public Result selectByMoPage(@RequestParam Integer pageNum,
                                 @RequestParam Integer pageSize,
                                 @RequestParam String name,
                                 @RequestParam String idcard) {
        QueryWrapper<Inpantient> queryWrapper = new QueryWrapper<Inpantient>().orderByDesc("id");    //默认倒叙，让最新的数据在最上面，需要正序的话删除.orderByDesc("id")
        queryWrapper.like(StrUtil.isNotBlank(name),"name",name);
        queryWrapper.like(StrUtil.isNotBlank(idcard),"idcard",idcard);
        Page<Inpantient> page = inpantientService.page(new Page<>(pageNum, pageSize), queryWrapper);
        List<Inpantient> records = page.getRecords();
        for(Inpantient record : records){
            Integer authorid = record.getAuthorid();
            User user = userService.getById(authorid);
            if(user != null){
                record.setUser(user.getName());
            }
        }
        return Result.success(page);
    }
    // timeRange: week month month2 month3

    @GetMapping("/lineCharts/{timeRange}")
    public Result lineCharts(@PathVariable String timeRange){
        return Result.success(inpantientService.getCountByTimeRange(timeRange));
    }

}
