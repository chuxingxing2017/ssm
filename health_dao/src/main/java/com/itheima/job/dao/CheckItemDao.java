package com.itheima.job.dao;

import com.github.pagehelper.Page;
import com.itheima.job.pojo.CheckItem;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/1/2
 */
@Repository
public interface CheckItemDao {

     void add(CheckItem checkItem);

     List<CheckItem> findPage(String queryString);

    Page<CheckItem> findPage2(String queryString) ;

    long findCountByCheckItemId(Integer id);

    void deleteById(Integer id);

    CheckItem findCheckItemById(Integer id);

    void edit(CheckItem checkItem);

    List<CheckItem> findAll();

    List<CheckItem> findCheckItemsByGroupId();

}
