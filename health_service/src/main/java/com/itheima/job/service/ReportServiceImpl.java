package com.itheima.job.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.job.dao.MemberDao;
import com.itheima.job.dao.OrderDao;
import com.itheima.job.dao.SetmealDao;

import com.itheima.job.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/1/31
 */
@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {

    //查询预约信息
    @Autowired
    OrderDao orderDao;

    //查询会员信息
    @Autowired
    MemberDao memberDao;

    @Autowired
    SetmealDao setmealDao;

    @Override
    public Map<String, Object> getBusinessReportData() {
        try {
            Map<String, Object> map = new HashMap<>();
            //当前月份日期
            String today = DateUtils.parseDate2String(DateUtils.getToday());
            //当前周第一天
            String firstDayOfWeek = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
            //当前周最后一天
            String lastDayOfWeek = DateUtils.parseDate2String(DateUtils.getSundayOfThisWeek());
            //当前月第一天
            String firstDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
            //当前月最后一天
            String lastDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getLastDay4ThisMonth());

            //获取当前日期
            String reportDate = today;
            //获取当前日期后注册的会员
            Integer todayNewMember = memberDao.findTodayNewMember(today);
            //获取所有的会员数量
            Integer totalMember = memberDao.findTotalMember();
            //获取本周的新增会员
            Integer thisWeekNewMember = memberDao.findNewMemerAfterDate(firstDayOfWeek);
            //获取本月的新增会员
            Integer thisMonthNewMember = memberDao.findNewMemerAfterDate(firstDay4ThisMonth);
            //获取当前日期预约人数
            Integer todayOrderNumber = orderDao.findTodayOrderNumber(today);
            //获取当前日期的到诊人数
            Integer todayVisitsNumber = orderDao.findTodayVisitsNumber(today);

            //本周第一和最后一天
            Map<String, String> betweenWeekDate = new HashMap<>();
            betweenWeekDate.put("first", firstDayOfWeek);
            betweenWeekDate.put("last", lastDayOfWeek);
            //本月第一和最后一天
            Map<String, String> betweenMonthDate = new HashMap<>();
            betweenMonthDate.put("first", firstDay4ThisMonth);
            betweenMonthDate.put("last", lastDay4ThisMonth);

            //获取本周的预约人数()
            Integer thisWeekOrderNumber = orderDao.findOrderNumberBetweenDate(betweenWeekDate);
            //获取当前周的到诊人数
            Integer thisWeekVisitsNumber = orderDao.findVisitsNumberBetweenDate(betweenWeekDate);
            //获取当前月的预约人数
            Integer thisMonthOrderNumber = orderDao.findOrderNumberBetweenDate(betweenMonthDate);
            //获取当前月的到诊人数
            Integer thisMonthVisitsNumber = orderDao.findVisitsNumberBetweenDate(betweenMonthDate);
            //获取热门套餐
            List<Map<String, Object>> hotSetmeal = setmealDao.findHotSetmeal();

            map.put("reportDate", today);
            map.put("todayNewMember",todayNewMember);
            map.put("totalMember",totalMember);
            map.put("thisWeekNewMember",thisWeekNewMember);
            map.put("thisMonthNewMember",thisMonthNewMember);
            map.put("todayOrderNumber",todayOrderNumber);
            map.put("todayVisitsNumber",todayVisitsNumber);
            map.put("thisWeekOrderNumber",thisWeekOrderNumber);
            map.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
            map.put("thisMonthOrderNumber",thisMonthOrderNumber);
            map.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
            map.put("hotSetmeal",hotSetmeal);

            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
