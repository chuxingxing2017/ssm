package com.itheima.job.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.job.constant.RedisConstant;
import com.itheima.job.dao.SetmealDao;
import com.itheima.job.entity.PageResult;
import com.itheima.job.entity.QueryPageBean;
import com.itheima.job.pojo.Setmeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/1/4
 */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //使用插件,初始化
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        //使用插件查询
        Page<Setmeal> page = setmealDao.findPage(queryPageBean.getQueryString());
        //封装返回数据
        return new PageResult(page.getTotal(), page.getResult());
    }
    /*
    *   向套餐表添加新数据
    * */
    @Override
    public void addSetmeal(Integer[] checkgroupIds, Setmeal setmeal) {
        //先添加套餐数据   ,使用selectkey返回插入的数据后返回的id
        setmealDao.addSetmeal(setmeal);
        if(checkgroupIds != null && checkgroupIds.length > 0) {
            //根据套餐id体检中间表
            addSetmealAndCheckgroupBySetmealId(setmeal.getId(), checkgroupIds);
        }
        //将图片名称保存到Redis
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
    }
    /*
    *   根据套餐id添加和检查组的中间表数据
    * */
    public void addSetmealAndCheckgroupBySetmealId(Integer id, Integer[] checkgroupIds) {

        if (checkgroupIds!=null &&checkgroupIds.length>0) {
            for (Integer checkgroupId : checkgroupIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("id", id);
                map.put("checkgroupid", checkgroupId);
                //遍历添加,每次添加一个
                setmealDao.addSetmealAndCheckgroupBySetmealId(map);
            }
        }
    }






    /*
    *   微信端获取套餐列表数据
    * */
    @Override
    public List<Setmeal> getSetmeal() {
        return setmealDao.getSetmeal();
    }

    /*
    *    联合查询套餐信息
    * */
    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.finById(id);
    }

    @Override
    public List<Map<String, Object>> getSetmealReport() {
        return setmealDao.findSetmealCount();
    }

}
