package com.itheima.health.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.job.constant.MessageConstant;
import com.itheima.job.constant.RedisMessageConstant;
import com.itheima.job.entity.Result;
import com.itheima.job.utils.SMSUtils;
import com.itheima.job.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/1/8
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeContorller {

    @Autowired
    private JedisPool jedisPool;
    /*
    *   发送手机验证码(预约套餐功能)
    * */
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){
        //获取手机号 ,生成随机激活码,使用阿里云发送短信
        Integer code = ValidateCodeUtils.generateValidateCode(4);
        try {
            //判断是否发送给成功
            SMSUtils.sendShortMessage(telephone,code.toString());
            //将验证码存到redis中,限制失效时间5分钟
            jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER, 5 * 60, code.toString());
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (ClientException e) {
            //验证码发送失败
            e.printStackTrace();
            return new Result(true, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }

    /*
     *   发送手机验证码(登陆用户功能)
     * */
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
        //获取手机号 ,生成随机激活码,使用阿里云发送短信
        Integer code = ValidateCodeUtils.generateValidateCode(4);
        try {
            //判断是否发送给成功
            SMSUtils.sendShortMessage(telephone,code.toString());
            //将验证码存到redis中,限制失效时间5分钟
            jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN, 5 * 60, code.toString());
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (ClientException e) {
            //验证码发送失败
            e.printStackTrace();
            return new Result(true, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }


}
