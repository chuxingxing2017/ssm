package com.itheima.job.service;

import com.itheima.job.entity.PageResult;
import com.itheima.job.entity.QueryPageBean;
import com.itheima.job.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/1/4
 */
public interface SetmealService {
    PageResult findPage(QueryPageBean queryPageBean);

    void addSetmeal(Integer[] chickgroupIds, Setmeal setmeal);

    List<Setmeal> getSetmeal();

    Setmeal findById(Integer id);

    List<Map<String,Object>> getSetmealReport();


}
