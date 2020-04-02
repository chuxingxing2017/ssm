package com.itheima.job.service;

import com.itheima.job.entity.PageResult;
import com.itheima.job.entity.QueryPageBean;
import com.itheima.job.execption.DeleteFailExecption;
import com.itheima.job.pojo.CheckItem;

import java.util.List;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description 检查项服务接口
 * @date 2020/1/2
 */
public interface CheckItemService {
    void add(CheckItem checkItem);

    PageResult findPage(QueryPageBean queryPageBean);

    void delete(Integer id) throws DeleteFailExecption;

    CheckItem findCheckItemById(Integer id);

    void edit(CheckItem checkItem);

    List<CheckItem> findAll();
}
