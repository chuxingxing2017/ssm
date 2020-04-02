package com.itheima.job.dao;

import com.itheima.job.pojo.Member;
import com.itheima.job.pojo.Order;

import java.util.Map;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/1/8
 */
public interface OrderDao {
    void add(Order member);

    Order findByMemberId(Integer id);

    Map findById(Integer id);

    Integer findTodayOrderNumber(String today);

    Integer findTodayVisitsNumber(String today);

    Integer findOrderNumberBetweenDate(Map<String, String> betweenWeekDate);

    Integer findVisitsNumberBetweenDate(Map<String, String> betweenWeekDate);
}
