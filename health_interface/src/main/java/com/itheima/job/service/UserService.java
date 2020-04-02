package com.itheima.job.service;

import com.itheima.job.pojo.User;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/1/31
 */
public interface UserService {
    User findUserByUsername(String username);
}
