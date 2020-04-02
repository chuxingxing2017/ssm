package com.itheima.job.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.job.constant.MessageConstant;
import com.itheima.job.constant.RedisConstant;
import com.itheima.job.entity.PageResult;
import com.itheima.job.entity.QueryPageBean;
import com.itheima.job.entity.Result;
import com.itheima.job.pojo.Setmeal;
import com.itheima.job.service.SetmealService;
import com.itheima.job.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/1/4
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile")MultipartFile imgfile){
        try {
            //获取原文件名
            String originalFilename = imgfile.getOriginalFilename();
            //获取文件后缀
            int i = originalFilename.lastIndexOf(".");
            String suffix = originalFilename.substring(i);
            //使用UUID随机产生文件名称,防止文件覆盖
            String fileName = UUID.randomUUID().toString()+suffix;
            //图片上传
            QiniuUtils.upload2Qiniu(imgfile.getBytes(),fileName);
            Result result = new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, fileName);
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
            return result;
        } catch (IOException e) {
            //上传失败
            e.printStackTrace();
            return  new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }
    }
    /*
    *   分页展示
    * */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult =  setmealService.findPage(queryPageBean);
        return pageResult;
    }


    /*
    *   添加套餐和中间表
    * */
    @RequestMapping("/add")
    public Result add(@RequestBody  Setmeal setmeal,Integer[] checkgroupIds){
        try {
            setmealService.addSetmeal(checkgroupIds,setmeal);
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
        /*System.out.println("从redis存数据");
        System.out.println("从数据库存数据");*/
    }

    /*
    *   根据id查询检查组信息
    * */
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Setmeal setmeal =  setmealService.findById(id);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.QUERY_ORDER_FAIL);
        }
    }

}
