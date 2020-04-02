package com.itheima.job.service;

import com.itheima.job.pojo.OrderSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/1/5
 */
public interface OrderSettingService {
    void add(ArrayList<OrderSetting> orderSettings);

    List<Map> findOrdersettingByate(String date);

    void editNumberByDate(OrderSetting orderSetting);
}
