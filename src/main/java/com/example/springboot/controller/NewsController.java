package com.example.springboot.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.HoneyLogs;
import com.example.springboot.common.LogType;
import com.example.springboot.common.Result;
import com.example.springboot.entity.News;
import com.example.springboot.entity.User;
import com.example.springboot.service.NewsService;
import com.example.springboot.service.UserService;
import com.example.springboot.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/news")
public class NewsController {

    @Autowired
    NewsService newsService;
    @Autowired
    UserService userService;

    /**
     * 新增信息
     */
    @HoneyLogs(operation = "新闻",type = LogType.ADD)
    @PostMapping("/add")
    public Result add(@RequestBody News news) {
        User currentUser = TokenUtils.getCurrentUser();  //获取当前登录的用户信息
        news.setAuthorid(currentUser.getId());
        news.setTime(DateUtil.now());     //  2023-11-30 16:37:50
        newsService.save(news);
        return Result.success();
    }

    /**
     * 修改信息
     */
    @HoneyLogs(operation = "新闻",type = LogType.UPDATE)
    @PutMapping("/update")
    public Result update(@RequestBody News news) {
        newsService.updateById(news);
        return Result.success();
    }

    /**
     * 删除信息
     */
    @HoneyLogs(operation = "新闻",type = LogType.DELETE)
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        newsService.removeById(id);
        return Result.success();
    }

    /**
     * 批量删除信息
     */
    @HoneyLogs(operation = "新闻",type = LogType.BATCH_DELETE)
    @DeleteMapping("/delete/batch")
    public Result batchdelete(@RequestBody List<Integer> ids) {
        newsService.removeBatchByIds(ids);
        return Result.success();
    }

    /**
     * 倒叙查询信息
     */
    @GetMapping("/selectall")
    public Result selectAll() {
        List<News> userList = newsService.list(new QueryWrapper<News>().orderByDesc("id"));
        return Result.success(userList);
    }

    /**
     * 根据id查询信息
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        News news = newsService.getById(id);
        User user = userService.getById(news.getAuthorid());
        if(user != null){
            news.setAuthor(user.getName());
        }
        return Result.success(news);
    }

    /**
     * 分页模糊查询
     * pageNum当前页码
     */
    @GetMapping("/selectByMoPage")
    public Result selectByMoPage(@RequestParam Integer pageNum,
                                 @RequestParam Integer pageSize,
                                 @RequestParam String title) {
        QueryWrapper<News> queryWrapper = new QueryWrapper<News>().orderByDesc("id");    //默认倒叙，让最新的数据在最上面，需要正序的话删除.orderByDesc("id")
        queryWrapper.like(StrUtil.isNotBlank(title),"title",title);
        Page<News> page = newsService.page(new Page<>(pageNum, pageSize), queryWrapper);
        List<News> records = page.getRecords();
        for(News record : records){
            Integer authorid = record.getAuthorid();
            User user = userService.getById(authorid);
            if(user != null){
                record.setAuthor(user.getName());
            }
        }
        return Result.success(page);
    }

}
