package com.itheima.job.service;

import com.itheima.job.entity.PageResult;
import com.itheima.job.entity.QueryPageBean;
import com.itheima.job.pojo.CheckGroup;

import java.util.List;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/1/3
 */
public interface CheckGroupService {
    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    PageResult findPage(QueryPageBean queryPageBean);

    CheckGroup findCheckGroupById(Integer id);

    List<Integer> findCheckitemByCheckGroupId(Integer id);


    void edit(CheckGroup checkGroup, Integer[] checkItemIds);

    void delete(Integer id);

    List<CheckGroup> findAll();
}
