package com.itheima.job.dao;

import com.github.pagehelper.Page;
import com.itheima.job.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/1/4
 */
public interface SetmealDao {
    Page<Setmeal> findPage(String queryString);

    void addSetmeal(Setmeal setmeal) ;

    void addSetmealAndCheckgroupBySetmealId(Map<String, Integer> map);

    List<Setmeal> getSetmeal();

    Setmeal finById(Integer id);


    List<Map<String,Object>> findSetmealCount();

    List<Map<String,Object>> findHotSetmeal();
}
