package com.itheima.health.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.job.constant.MessageConstant;
import com.itheima.job.constant.RedisMessageConstant;
import com.itheima.job.entity.Result;
import com.itheima.job.pojo.Order;
import com.itheima.job.service.OrderService;
import com.itheima.job.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/1/8
 */
@RestController
@RequestMapping("/order")
public class OrderMobileController {

    @Reference
    private OrderService orderService;
    @Autowired
    private JedisPool jedisPool;

    /*
    *     体检预约
    * */
    @RequestMapping("/submit")
    public Result submitOrder(@RequestBody Map map){//map接受表表单上的所有数据,封装成map
        //获取手机号,校验验证码
        String telephone = (String)map.get("telephone");
        //获取jidis数据库中的验证码
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        //获取界面的验证码
        String validateCode = (String)map.get("validateCode");
        //比较验证码是否正确
        if (codeInRedis  == null || !codeInRedis.equals(validateCode)) {
            //验证码错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        Result result =null;
        //验证码正确,进行预约业务
        try {
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            //预约方法,放回result
            result = orderService.order(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "预约失败,服务器异常");
        }
        if(result.isFlag()){
            //预约成功，发送短信通知，短信通知内容可以是“预约时间”，“预约人”，“预约地点”，“预约事项”等信息。
            String orderDate = (String) map.get("orderDate");
            try {
                SMSUtils.sendShortMessage(telephone,orderDate);
            } catch (ClientException e) {
                e.printStackTrace();
                return new Result(false, "预约成功,但是短息发送不出去");
            }
        }
        return result;
    }
}
