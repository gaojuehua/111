package com.example.springboot.utils;

import com.example.springboot.controller.ILKPO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 工具类
 */
public class Tools {

    private static DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 冒泡排序 根据日期字段数据进行从小到大排序
     * @param arr
     * @return
     * @throws Exception
     */
    public static List<ILKPO> bubbleSortOpt(List<ILKPO> arr) throws Exception {

        if (arr == null) {
            throw new Exception("数组为空");
        }
        if (arr.size() < 2) {
            return arr;
        }
        ILKPO temp;
        for (int i = 0; i < arr.size() - 1; i++) {
            for (int j = 0; j < arr.size() - i - 1; j++) {
                Date data1 = fmt.parse(arr.get(j).getDate());
                Date data2 = fmt.parse(arr.get(j + 1).getDate());
                //如果大于0  则data1 > data2
                if (data1.compareTo(data2) > 0) {
                    temp = arr.get(j);
                    arr.set(j, arr.get(j + 1));
                    arr.set(j + 1, temp);
                }
            }
        }
        return arr;
    }
}
