package com.example.springboot.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.example.springboot.common.AuthAccess;
import com.example.springboot.common.HoneyLogs;
import com.example.springboot.common.LogType;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Inpantient;
import com.example.springboot.entity.User;
import com.example.springboot.service.InpantientService;
import com.example.springboot.service.OrdersService;
import com.example.springboot.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class WebController {

    @Resource
    UserService userService;

    @Resource
    InpantientService inpantientService;

    @Resource
    OrdersService ordersService;

    @AuthAccess
    @GetMapping("/")
    public Result hello() {
        return Result.success("success");
    }

    @HoneyLogs(operation = "用户",type = LogType.LOGIN)
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
//        if (user.getUsername() == null || user.getUsername().isEmpty()){
//            return Result.error("数据输入有误");
//        }
        if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword())) {
            return Result.error("数据输入有误");
        }
        user = userService.login(user);
        return Result.success(user);
    }

    @HoneyLogs(operation = "用户",type = LogType.REGISTER)
    @AuthAccess
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword()) || StrUtil.isBlank(user.getRole())) {
            return Result.error("数据输入有误");
        }
        if (user.getUsername().length() > 10 || user.getPassword().length() > 20) {
            return Result.error("数据输入过长");
        }
        user = userService.register(user);
        return Result.success(user);
    }


    /*重置密码*/
    @HoneyLogs(operation = "用户",type = LogType.UPDATE)
    @AuthAccess
    @PutMapping("/password")
    public Result password(@RequestBody User user) {
        if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPhone())) {
            return Result.error("数据输入有误");
        }
            userService.resetPassword(user);
            return  Result.success();

    }

    /**
     * 统计图数据*/
    @GetMapping("/charts")
    public Result charts(){
        //包装折线图数据
        List<Inpantient> list = inpantientService.list();
        Set<String> dates = list.stream().map(Inpantient::getTime).collect(Collectors.toSet());   //先去重   Set无序
       List<String> dataList = CollUtil.newArrayList(dates);
       dataList.sort(Comparator.naturalOrder());  //排序




        List<Dict> lineList = new ArrayList<>();
        for(String data : dataList){
            Set<String> sum = list.stream().filter(inpantient -> inpantient.getTime().equals(data)).map(Inpantient::getIlk).collect(Collectors.toSet());
            Dict dict = Dict.create();
            Dict line = dict.set("data", data).set("value", sum);
            lineList.add(line);
        }

//        //柱状图
//        Set<String> categories = list.stream().map(Inpantient::getIlk).collect(Collectors.toSet());   //先去重   Set无序
//        List<Dict> barList = new ArrayList<>();
//        for(String cate : categories){
//
//            BigDecimal sum = list.stream().filter(inpantient -> inpantient.getIlk().equals(cate)).map(Inpantient::getCategory).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
//            Dict dict = Dict.create();
//            Dict bar = dict.set("ilk", cate).set("value", sum);
//            barList.add(bar);
//        }
//
//       // 包装所有数据
//        Dict res = Dict.create().set("line", lineList).set("bar", barList);
        return Result.success(lineList);

    }

//    public List<String> datetimeToDateStr(List<DateTime> dateTimeList){
//        List<String> list = CollUtil.newArrayList();
//        if(CollUtil.isEmpty(dateTimeList)){
//            return list;
//        }
//        for (DateTime dateTime : dateTimeList){
//            String date = DateUtil.formatDate(dateTime);
//            list.add(date);
//        }
//        return list;
//    }

}





































//    @PostMapping("/post")   //    //有了@RequestBody之后是json
//    public Result post(@RequestBody Obj obj){
//        System.out.println(obj.getName() == null ? "是null" : obj.getName().isEmpty());   //使用一个对象之前判断是否为空
//        return Result.success(obj);
//    }
//    @PutMapping("/put")
//    public Result put(@RequestBody Obj obj){
//
//        return Result.success(obj);
//    }
//    @RequestMapping(method = RequestMethod.DELETE)
//    public Result delete(Obj obj){
//
//        return Result.success(obj);
//    }
//    @DeleteMapping("/delete")   //路径参数删除                delete可以传json                   http://localhost:9090/web/delete/1   /1 =>  /{id}
//    public Result delete(@RequestBody List<Integer> ids){  // 括号中可以写(@RequestBody Obj obj)或(@PathVariable Integer id)        (@RequestBody List<Integer> ids)批量删除
//
//        return Result.success(ids);
//    }
//    // post 新增数据
//    // put  更新更改
//    // delete 删除
//
//    //  404 路由与后台定义不匹配
//    //  405 接口匹配类型不匹配
//    //  500 空指针异常

