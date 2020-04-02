package com.itheima.job.dao;

import com.itheima.job.pojo.Order;
import com.itheima.job.pojo.OrderSetting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/1/5
 */
public interface OrderSettingDao {

    int findCountByOrderSettingDate(Date orderDate);

    void editOrderSettingByFile(OrderSetting orderSetting);

    void addByFile(OrderSetting orderSetting);

    List<OrderSetting> findOrdersettingByate(String date);

    OrderSetting findOrdersettingBydate(String orderDate);

    void editReservationsByOrderDate(String orderDate);
}
