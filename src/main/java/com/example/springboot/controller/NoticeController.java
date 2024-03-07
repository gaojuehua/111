package com.example.springboot.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.HoneyLogs;
import com.example.springboot.common.LogType;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Notice;
import com.example.springboot.entity.User;
import com.example.springboot.service.NoticeService;
import com.example.springboot.service.UserService;
import com.example.springboot.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    NoticeService noticeService;
    @Autowired
    UserService userService;

    /**
     * 新增信息
     */
    @HoneyLogs(operation = "公告" , type = LogType.ADD)
    @PostMapping("/add")
    public Result add(@RequestBody Notice notice) {
        User currentUser = TokenUtils.getCurrentUser();  //获取当前登录的用户信息
        notice.setUserid(currentUser.getId());
        notice.setTime(DateUtil.now());     //  2023-11-30 16:37:50
        noticeService.save(notice);
        return Result.success();
    }

    /**
     * 修改信息
     */
    @HoneyLogs(operation = "公告", type = LogType.UPDATE)
    @PutMapping("/update")
    public Result update(@RequestBody Notice notice) {
        noticeService.updateById(notice);
        return Result.success();
    }

    /**
     * 删除信息
     */
    @HoneyLogs(operation = "公告",type = LogType.DELETE)
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        noticeService.removeById(id);
        return Result.success();
    }

    /**
     * 批量删除信息
     */
    @HoneyLogs(operation = "公告", type = LogType.BATCH_DELETE)
    @DeleteMapping("/delete/batch")
    public Result batchdelete(@RequestBody List<Integer> ids) {
        noticeService.removeBatchByIds(ids);
        return Result.success();
    }

    /**
     * 查询用户信息
     */
    @GetMapping("/selectall")
    public Result selectAll() {
        List<Notice> ordersList = noticeService.list(new QueryWrapper<Notice>().orderByDesc("id"));
        return Result.success(ordersList);
    }

    /**
     * 查询用户公告
     * */
    @GetMapping("/selectUserData")
    public Result selectUserData() {
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<Notice>().orderByDesc("id");
        queryWrapper.eq("open",1);   //用户只能看到公开的公告数据
        List<Notice> userList = noticeService.list(queryWrapper);
        return Result.success(userList);
    }

    /**
     * 根据id查询用户信息
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Notice notice = noticeService.getById(id);
        User user = userService.getById(notice.getUserid());
        if(user != null){
            notice.setUser(user.getName());
        }
        return Result.success(notice);
    }

    /**
     * 分页模糊查询
     * pageNum当前页码
     */
    @GetMapping("/selectByMoPage")
    public Result selectByMoPage(@RequestParam Integer pageNum,
                                 @RequestParam Integer pageSize,
                                 @RequestParam String title) {
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<Notice>().orderByDesc("id");    //默认倒叙，让最新的数据在最上面，需要正序的话删除.orderByDesc("id")
        queryWrapper.like(StrUtil.isNotBlank(title),"title",title);
        Page<Notice> page = noticeService.page(new Page<>(pageNum, pageSize), queryWrapper);
        List<Notice> records = page.getRecords();
        for(Notice record : records){
            Integer authorid = record.getUserid();
            User user = userService.getById(authorid);
            if(user != null){
                record.setUser(user.getName());
            }
        }
        return Result.success(page);
    }

}
