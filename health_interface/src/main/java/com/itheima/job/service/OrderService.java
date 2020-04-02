package com.itheima.job.service;

import com.itheima.job.entity.Result;

import java.text.ParseException;
import java.util.Map; /**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/1/8
 */
public interface OrderService {
    Result order(Map map) throws ParseException;

    Map findById(Integer id) throws Exception;

}
