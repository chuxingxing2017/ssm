package com.itheima.job.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.job.constant.MessageConstant;
import com.itheima.job.entity.Result;
import com.itheima.job.pojo.OrderSetting;
import com.itheima.job.service.OrderSettingService;
import com.itheima.job.utils.POIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/1/5
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    OrderSettingService orderSettingService;

    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile")MultipartFile excelFile ){

        try {
            //读取Excek文件数据,获得所有行的信息集合
            List<String[]> list = POIUtils.readExcel(excelFile);
            //用于使用ordersetting封装所有的行信息
            ArrayList<OrderSetting> orderSettings = new ArrayList<>();
            //遍历文件行数据
            if (list != null && list.size() > 0) {

                for (String[] strings : list) {
                    //将每一行的数据封装到一个ordersetting对象  2020/3/6
                    OrderSetting orderSetting = new OrderSetting(new Date(strings[0]), Integer.parseInt(strings[1]));
                    //将每个行对象添加到对象集合中
                    orderSettings.add(orderSetting);
                }
                orderSettingService.add(orderSettings);
            }
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    @RequestMapping("/findOrdersettingByate")
    public Result findOrdersettingByate(@RequestParam("date") String date) {

        try {
            List<Map> list = orderSettingService.findOrdersettingByate(date);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }


    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting) {

        try {
            orderSettingService.editNumberByDate(orderSetting);
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.ORDERSETTING_FAIL);
        }
    }
}
