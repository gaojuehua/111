package com.example.springboot.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.controller.ILKPO;
import com.example.springboot.entity.Inpantient;
import com.example.springboot.entity.Orders;
import com.example.springboot.mapper.InpantientMapper;
import com.example.springboot.utils.Tools;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class InpantientService extends ServiceImpl<InpantientMapper, Inpantient> {

    @Resource
    InpantientMapper inpantientMapper;

    @Override
    public List<Inpantient> list() {
        return inpantientMapper.selectList(new QueryWrapper<>());
    }

    @Transactional
    public Map<String, Object> getCountByTimeRange(String timeRange) throws Exception {

        Map<String, Object> map = new HashMap<>();
        Date today = new Date();
        List<DateTime> dateRange;
        switch (timeRange) {
            case "week":

                // offsetDay 计算时间的一个工具方法
                // rangeToList 返回从开始时间到结束时间的一个时间范围

                dateRange = DateUtil.rangeToList(DateUtil.offsetDay(today, -6), today, DateField.DAY_OF_WEEK);
                break;
            case "month":
                dateRange = DateUtil.rangeToList(DateUtil.offsetDay(today, -29), today, DateField.DAY_OF_MONTH);
                break;
            case "month2":
                dateRange = DateUtil.rangeToList(DateUtil.offsetDay(today, -59), today, DateField.DAY_OF_MONTH);
                break;
            case "month3":
                dateRange = DateUtil.rangeToList(DateUtil.offsetDay(today, -89), today, DateField.DAY_OF_MONTH);
                break;
            default:
                dateRange = new ArrayList<>();
        }
        //  datetimeToDateStr() 就是一个处理的方法， 把 DateTime类型的List转换成一个 String的List

        List<String> dateStrRange = datetimeToDateStr(dateRange);
        map.put("date", dateStrRange);  // x轴的日期数据生产完毕

        // getCountByTimeRange 不会统计数据库没有的日期，比如 数据库 11.4 这一天数据没有，他不会返回 date=2022-11-04,count=0 这个数据
        // 使用冒泡排序 根据日期字段对数据进行排序

        List<ILKPO> yichuanCount = Tools.bubbleSortOpt(inpantientMapper.getCountByTimeRange(timeRange,1));
        List<ILKPO> xijunCount = Tools.bubbleSortOpt(inpantientMapper.getCountByTimeRange(timeRange, 2));
        List<ILKPO> bingduCount = Tools.bubbleSortOpt(inpantientMapper.getCountByTimeRange(timeRange, 3));
        List<ILKPO> hanjianCount = Tools.bubbleSortOpt(inpantientMapper.getCountByTimeRange(timeRange, 4));
        List<ILKPO> jijieCount = Tools.bubbleSortOpt(inpantientMapper.getCountByTimeRange(timeRange, 5));
        map.put("yichuan", countList(yichuanCount, dateStrRange));
        map.put("xijun", countList(xijunCount, dateStrRange));
        map.put("bingdu", countList(bingduCount, dateStrRange));
        map.put("hanjian", countList(hanjianCount, dateStrRange));
        map.put("jijie", countList(jijieCount, dateStrRange));
        return map;
    }
    // 把 DateTime类型的List转换成一个 String的List
    private List<String> datetimeToDateStr(List<DateTime> dateTimeList) {
        List<String> list = CollUtil.newArrayList();
        if (CollUtil.isEmpty(dateTimeList)) {
            return list;
        }
        for (DateTime dateTime : dateTimeList) {
            String date = DateUtil.formatDate(dateTime);
            list.add(date);
        }
        return list;
    }

    // 对数据库未统计的时间进行处理
    private List<Integer> countList(List<ILKPO> countPOList, List<String> dateRange) {
        List<Integer> list = CollUtil.newArrayList();
        if (CollUtil.isEmpty(countPOList)) {
            return list;
        }
        for (String date : dateRange) {
            // .map(IlkPO::getCount) 取出 对象里的 count值
            // orElse(0) 对没匹配的数据返回0
            // "2022-11-04" 没有的话 就返回0
            Integer count = countPOList.stream().filter(countPO -> date.equals(countPO.getDate()))
                    .map(ILKPO::getCount).findFirst().orElse(0);
            list.add(count);
        }
        return list;
    }

}
