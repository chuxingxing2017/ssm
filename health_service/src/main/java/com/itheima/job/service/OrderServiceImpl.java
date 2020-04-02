package com.itheima.job.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.job.constant.MessageConstant;
import com.itheima.job.dao.MemberDao;
import com.itheima.job.dao.OrderDao;
import com.itheima.job.dao.OrderSettingDao;
import com.itheima.job.entity.Result;
import com.itheima.job.pojo.Member;
import com.itheima.job.pojo.Order;
import com.itheima.job.pojo.OrderSetting;
import com.itheima.job.utils.DateUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/1/8
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderDao orderDao;//用来插叙预约表(t_order)

    @Autowired
    OrderSettingDao orderSettingDao;//用来查询预约时期表(t_ordersetting)

    @Autowired
    MemberDao memberDao;//用来查询会员表(t_member)
    /*
    *       体检预约  返回result  封装查询信息以及会员id
    * */
    @Override
    public Result order(Map map) throws ParseException {
        //判断当前预约套餐日期是否是有效期内
        OrderSetting orderSetting = orderSettingDao.findOrdersettingBydate((String) map.get("orderDate"));
        if (orderSetting == null) {
            //当前日期不可以预约
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //判断当前日期能否还能预约(人数是否已经满了)
        if (orderSetting.getNumber() <= orderSetting.getReservations()) {
            //预约人数已满
            return new Result(false, MessageConstant.ORDER_FULL);
        }
        //判断当前用户是否是会员
        Member member = memberDao.findMemberByTelephone((String) map.get("telephone"));
        if (member == null) {
            //不是会员,创建会员添加到数据库中
            member = new Member();
            member.setPhoneNumber((String)map.get("telephone"));
            member.setName((String)map.get("name"));
            member.setSex((String)map.get("sex"));
            member.setIdCard((String)map.get("idCard"));
            member.setRegTime(new Date());
            memberDao.add(member);
        }
        //是会员,判断当前套餐预约时间是否已经预约
        Order order = orderDao.findByMemberId(member.getId());
        if (order!=null) {
            //当前日期已经预约
            return new Result(false, MessageConstant.HAS_ORDERED);
        }
        //未预约,将预约信息添加到预约表,并且预约设置表的预约数量加一
        order = new Order(member.getId(),orderSetting.getOrderDate(),(String) map.get("orderType"),Order.ORDERSTATUS_NO,Integer.parseInt((String) map.get("setmealId")));

        orderDao.add(order);
        orderSettingDao.editReservationsByOrderDate((String) map.get("orderDate"));
        return new Result(true, MessageConstant.ORDER_SUCCESS,order);
    }

    @Override
    public Map findById(Integer id) throws Exception {
        Map map  = orderDao.findById(id);
        if(map != null){
            //处理日期格式
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate", DateUtils.parseDate2String(orderDate));
        }
        return map;
    }
}
