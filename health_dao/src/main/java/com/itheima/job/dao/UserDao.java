package com.itheima.job.dao;

import com.itheima.job.pojo.User;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/1/31
 */
public interface UserDao {
    User findUserByUsername(String username);
}
