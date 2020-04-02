package com.itheima.job.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.job.constant.MessageConstant;
import com.itheima.job.entity.PageResult;
import com.itheima.job.entity.QueryPageBean;
import com.itheima.job.entity.Result;
import com.itheima.job.execption.DeleteFailExecption;
import com.itheima.job.pojo.CheckItem;
import com.itheima.job.service.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description 体检检查项管理
 * @date 2020/1/2
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {

    @Reference
    CheckItemService checkItemService;

    /*
     *   添加数据
     * */
    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")
    public Result add(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.add(checkItem);
            return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
    }

    /*
     *   分页展示数据
     * */
    @RequestMapping("/findPage")
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = checkItemService.findPage(queryPageBean);
        return pageResult;
    }

    /*
     *   删除数据
     * */
    @RequestMapping("/delete")
    @PreAuthorize("hasAuthority('CHECK_DELETE')")
    public Result delete(@RequestParam("id") Integer id) {
        System.out.println(id);
        try {
            checkItemService.delete(id);
            //可以删除
            return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
        } catch (DeleteFailExecption e) {
            //被关联,不可以删除
            return new Result(false, MessageConstant.DELETE_CHECKGROUPMORE_FAIL);
        } catch (Exception e) {
            //异常,删除失败
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
    }

    @RequestMapping("/findCheckItemById")
    public Result findCheckItemById(Integer id){
        //根据id查询检查项
        try {
            CheckItem checkItem = checkItemService.findCheckItemById(id);
            return new Result(true, "回显成功", checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "回显失败");
        }
    }

    @RequestMapping("/edit")
    @PreAuthorize("hasAuthority('CHECKITEM_EDIT')")
    public Result edit(@RequestBody CheckItem checkItem){
        //根据id查询检查项
        try {
            checkItemService.edit(checkItem);
            return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL);
        }
    }

    @RequestMapping("/findAll")
    public Result findAll() {
        try {
            List<CheckItem> list = checkItemService.findAll();
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

}
