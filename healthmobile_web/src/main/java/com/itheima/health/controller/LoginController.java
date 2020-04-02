package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.job.constant.MessageConstant;
import com.itheima.job.constant.RedisMessageConstant;
import com.itheima.job.entity.Result;
import com.itheima.job.pojo.Member;
import com.itheima.job.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description 手机号登陆界面登陆功能
 * @date 2020/1/27
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @Reference
    MemberService memberService;
    @Autowired
    JedisPool jedisPool;

    /**
     * 手机号登陆界面登陆功能
     * */
    @RequestMapping("/check")
    public Result check(HttpServletResponse response, @RequestBody Map map){
        //获取验证码和手机号
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        //获取redis中的验证码和界面的验证码进行比对
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        if (codeInRedis == null || !codeInRedis.equals(validateCode)) {
            //验证码错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //验证码正确,判断用户是否是会员,不是会员注册会员
        Member member = memberService.findByTelephone(telephone);
        if (member == null) {
            //不是会员,注册会员
            member = new Member();
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            memberService.add(member);
        }
        //登陆成功
        //将会员电话信息存入到cookie中,追踪用户,用于分布式系统单点登录
        Cookie cookie = new Cookie("login_member_telephone",telephone);
        cookie.setPath("/");//路径
        cookie.setMaxAge(60*60*60*24);//有效期30天(单位秒)
        response.addCookie(cookie);
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }
}
