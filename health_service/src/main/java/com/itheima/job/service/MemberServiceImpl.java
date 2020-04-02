package com.itheima.job.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.job.dao.MemberDao;
import com.itheima.job.pojo.Member;
import com.itheima.job.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/1/27
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService{
    @Autowired
    MemberDao memberDao;
    /**
     * 根据电话号码查询会员信息
    * */
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findMemberByTelephone(telephone);
    }
    /**
    * 向会员表中添加会员数据
    * */
    @Override
    public void add(Member member) {
        //如果member有密码,用MD5加密
        if (member.getPassword()!=null) {
            //加密
            member.setPassword(MD5Utils.md5(member.getPassword()));
        }
        memberDao.add(member);
    }


    public List<Integer> findMemberCountByMonth(List<String> list) {
        List<Integer> memberCount = new ArrayList<>();
        for (String month : list) {
            month = month + "-31";
            Integer count = memberDao.findMemberCountByMonth(month);
            memberCount.add(count);
        }
        return memberCount;
    }
}
