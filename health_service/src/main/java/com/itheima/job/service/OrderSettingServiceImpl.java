package com.itheima.job.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.job.dao.OrderSettingDao;
import com.itheima.job.pojo.OrderSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/1/5
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    OrderSettingDao orderSettingDao;

    /*
     *   批量导入ordersetting信息
     * */
    @Override
    public void add(ArrayList<OrderSetting> orderSettings) {
        for (OrderSetting orderSetting : orderSettings) {
            int count = orderSettingDao.findCountByOrderSettingDate(orderSetting.getOrderDate());
            System.out.println(count);
            if (count > 0) {
                //数据库已存在日期,进行修改操作
                orderSettingDao.editOrderSettingByFile(orderSetting);
            } else {
                //数据库中没有日期信息
                orderSettingDao.addByFile(orderSetting);
            }
        }
    }

    @Override
    public List<Map> findOrdersettingByate(String date) {
        //返回当月的数据
        String[] split = date.split("-");
        if (Integer.parseInt(split[1]) < 10) {
            date = split[0] + "-0" + split[1];
        }
        List<OrderSetting> list = orderSettingDao.findOrdersettingByate(date);
        //封装到Map集合中
        List<Map> data = new ArrayList<>();
        for (OrderSetting orderSetting : list) {
            Map map = new HashMap<>();
            map.put("date", orderSetting.getOrderDate().getDate());
            map.put("number", orderSetting.getNumber());
            map.put("reservations", orderSetting.getReservations());
            data.add(map);
        }
        return data;
    }

    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        long count = orderSettingDao.findCountByOrderSettingDate(orderSetting.getOrderDate());
        if(count > 0){
            //当前日期已经进行了预约设置，需要进行修改操作
            orderSettingDao.editOrderSettingByFile(orderSetting);
        }else{
            //当前日期没有进行预约设置，进行添加操作
            orderSettingDao.addByFile(orderSetting);
        }
    }


}
