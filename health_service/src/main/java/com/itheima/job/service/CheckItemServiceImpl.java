package com.itheima.job.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.job.dao.CheckItemDao;
import com.itheima.job.entity.PageResult;
import com.itheima.job.entity.QueryPageBean;
import com.itheima.job.execption.DeleteFailExecption;
import com.itheima.job.pojo.CheckItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/1/2
 */
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService{
    @Autowired
    CheckItemDao checkItemDao;

    /*
    *   添加检查项
    * */
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //使用pagehelp插件,相当于在所有的查询操作后面加了limit语句,多数据库通用
        //初始化分页数据信息
       /* PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //调用dao层查询数据
        System.out.println(checkItemDao);
        List<CheckItem> rows = checkItemDao.findPage(queryPageBean.getQueryString());
        //使用插件手机结果集信息
        PageInfo<CheckItem> pageInfo = new PageInfo(rows);
        ///封装结果集
        return new PageResult(pageInfo.getTotal(), pageInfo.getList());*/

       //第二种插件查询的方式
        //初始化分页查询数据
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //使用插件查询结果
        Page<CheckItem> page = checkItemDao.findPage2(queryPageBean.getQueryString());
        //封装结果集
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void delete(Integer id) throws DeleteFailExecption {
        //不能直接删除数据是,先检测是否与中间表关联,不关联在删除
        long count = checkItemDao.findCountByCheckItemId(id);
        if (count > 0) {
            //数据与中间表有关联,不允许删除,抛出异常提示上一层
            throw new DeleteFailExecption();
        }
        checkItemDao.deleteById(id);
    }

    @Override
    public CheckItem findCheckItemById(Integer id) {
        return checkItemDao.findCheckItemById(id);
    }

    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }
}
