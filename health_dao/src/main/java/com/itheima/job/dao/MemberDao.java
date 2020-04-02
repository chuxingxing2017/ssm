package com.itheima.job.dao;

import com.itheima.job.pojo.Member;

import java.util.Date;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/1/8
 */
public interface MemberDao {
    Member findMemberByTelephone(String telephone);

    void add(Member member);

    Integer findMemberCountByMonth(String month);

    Integer findTotalMember();

    Integer findNewMemerAfterDate(String firstDayOfWeek);

    Integer findTodayNewMember(String today);
}
