package com.itheima.job.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.job.pojo.Permission;
import com.itheima.job.pojo.Role;
import com.itheima.job.pojo.User;
import com.itheima.job.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Set;
@Component
public class SpringSecurityUserService implements UserDetailsService {

    @Reference //注意：此处要通过dubbo远程调用用户服务
    private UserService userService;

    //认证:执行/login.do的表单认证的时候,就会执行该代码,传递username(用户名),返回认证封装的信息UserDetails对象
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.根据用户名,获取用户信息(包含角色集合,权限集合)
        User user = userService.findUserByUsername(username);
        //判断是否存在
        if (user == null) {
            //用户不存在,不能登陆,跳转到登陆界面
            return null;//用户名输入错误,抛出异常
        }
        //2.使用登录名,查询角色,权限信息
        //配置权限角色信息
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        //根据用户信息查询角色信息
        Set<Role> roles = user.getRoles();
        if (roles!=null&&roles.size()>0) {
            for (Role role : roles) {
                //添加角色
                authorities.add(new SimpleGrantedAuthority(role.getKeyword()));
                //遍历角色中的权限信息
                Set<Permission> permissions = role.getPermissions();
                if (permissions!=null&&permissions.size()>0) {
                    for (Permission permission : permissions) {
                        //添加权限信息
                        authorities.add(new SimpleGrantedAuthority(permission.getKeyword()));
                    }
                }
            }
        }


        /*返回数据参数
         * 1.存放当前登录名
         * 2.存放当前数据的密码,会进行自动比对,如果密码一直,登陆成功;
         *  如果密码不一致,抛出异常,会回退到登陆界面,
         * 3.授权的角色,权限集合
         * */

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}
