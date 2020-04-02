package com.itheima.job.dao;

import com.itheima.job.pojo.Permission;

import java.util.Set;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/1/31
 */
public interface PermissionDao {
    Set<Permission> findPermissionByRoleId(Integer roleId);
}
