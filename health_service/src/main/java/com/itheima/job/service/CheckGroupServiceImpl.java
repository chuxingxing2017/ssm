package com.itheima.job.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.job.dao.CheckGroupDao;
import com.itheima.job.entity.PageResult;
import com.itheima.job.entity.QueryPageBean;
import com.itheima.job.pojo.CheckGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/1/3
 */
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    CheckGroupDao checkGroupDao;

    @Override
    //添加检查组合，同时需要设置检查组合和检查项的关联关系
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupDao.add(checkGroup);
        setCheckGroupAndCheckItem(checkGroup.getId(), checkitemIds);
    }


    //设置检查组合和检查项的关联关系，向中间表中添加数据
    private void setCheckGroupAndCheckItem(Integer id, Integer[] checkitemIds) {
        if (checkitemIds != null &&checkitemIds.length > 0) {
            //遍历检查组关联的检查项,封装到集合中,并添加到中间表
            //遍历每次添加一个,id不变,checkitemid变
            for (Integer checkitemId : checkitemIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("checkgroup_id",id);
                map.put("checkitem_id",checkitemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //使用插件,初始化插件信息
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //使用插件进行查询
        Page<CheckGroup> page = checkGroupDao.findPage(queryPageBean.getQueryString());
        //封装数据并返回
        return new PageResult(page.getTotal(), page.getResult());
    }

    /*
     *   根据检查组id查询检查组信息
     * */
    @Override
    public CheckGroup findCheckGroupById(Integer id) {
        return checkGroupDao.findCheckGroupById(id);
    }

    /*
     *   根据检查id查询检查项信息
     * */
    @Override
    public List<Integer> findCheckitemByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckitemByCheckGroupId(id);
    }

    /*
    *   保存编辑检查组数据
    * */
    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkItemIds) {
        checkGroupDao.edit(checkGroup);
        deleteCheckGroupAndItemById(checkGroup.getId());
        setCheckGroupAndCheckItem(checkGroup.getId(),checkItemIds);
    }



    /*
    *   根据检查组的id删除中间表数据
    * */
    public void deleteCheckGroupAndItemById(Integer id) {
        checkGroupDao.deleteCheckGroupAndItemById(id);
    }

    /*
    *  根据检查组id删除中间表和检查组的数据
    * */
    @Override
    public void delete(Integer id) {
        //先删除中间表
        checkGroupDao.deleteCheckGroupAndItemById(id);
        //在删除检查组中的数据
        checkGroupDao.deleteCheckGroupById(id);
    }

    @Override
    public List<CheckGroup> findAll() {

        return checkGroupDao.findAll();
    }

}
