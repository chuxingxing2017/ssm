package com.itheima.job.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.job.constant.MessageConstant;
import com.itheima.job.entity.Result;

import com.itheima.job.service.UserService;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/1/31
 */
@RestController
@RequestMapping("/user")
public class UserConreoller {

    @Reference
    UserService userService;

    @RequestMapping("/getusername")
    public Result getUsername() {
        try {
            //通过springsecurity,获取用户信息
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            com.itheima.job.pojo.User u = userService.findUserByUsername(user.getUsername());
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, u.getUsername() + "(" + u.getGender() + ")");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
    }
}
