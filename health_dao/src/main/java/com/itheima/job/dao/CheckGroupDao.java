package com.itheima.job.dao;

import com.github.pagehelper.Page;
import com.itheima.job.pojo.CheckGroup;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/1/3
 */
@Repository
public interface CheckGroupDao {

    void add(CheckGroup checkGroup);

    void setCheckGroupAndCheckItem(Map<String, Integer> map);

    Page<CheckGroup> findPage(String queryString);

    CheckGroup findCheckGroupById(Integer id);

    List<Integer> findCheckitemByCheckGroupId(Integer id);

    void edit(CheckGroup checkGroup);

    void deleteCheckGroupAndItemById(Integer id);


    void deleteCheckGroupById(Integer id);

    List<CheckGroup> findAll();

    CheckGroup findGroupAndItemBySetmealId(Integer id);
}
