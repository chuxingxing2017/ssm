package com.itheima.job.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.job.constant.MessageConstant;
import com.itheima.job.entity.Result;
import com.itheima.job.pojo.Order;
import com.itheima.job.service.MemberService;
import com.itheima.job.service.OrderService;
import com.itheima.job.service.ReportService;
import com.itheima.job.service.SetmealService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Mr.CHU
 * @version 1.0
 * @description ${PACKAGE}
 * @date 2020/2/1
 */
@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    MemberService memberService;

    @Reference
    SetmealService setmealService;

    @Reference
    ReportService reportService;



    /*
    *   获取会员报表(折现图)
    * */
    @RequestMapping("/getMemberReport")
    public Result getMemberReport() {
        try {
            //查找当前日期的前一年12的信息
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH,-12);//获取当前日期之前的1个月的日期
            //获取日期集合
            List<String> list = new ArrayList<>();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
            for (int i = 0; i < 12; i++) {
                calendar.add(Calendar.MONTH,1);
                list.add(simpleDateFormat.format(calendar.getTime()));
            }
            //将日期集合封装到map中,返回的Result中
            Map<String, List> map = new HashMap<>();
            map.put("months", list);
            //从数据库中查找,会员数量信息
            List<Integer> memberCount = memberService.findMemberCountByMonth(list);
            map.put("memberCount", memberCount);

            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    /*
    *   获取检查组报表(饼图)
    * */
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport() {
        try {
            //创建封装数据的map
            Map<String, Object> map = new HashMap<>();
            //数据库查询套餐名称,和预约数量
            List<Map<String,Object>> setmealcount = setmealService.getSetmealReport();
            //遍历setmealcount,获取setmaealname
            ArrayList<String> setmaealname = new ArrayList<>();
            for (Map<String, Object> objectMap : setmealcount) {
                String name = (String) map.get("name");
                setmaealname.add(name);
            }
            map.put("setmaealname",setmaealname);
            map.put("setmaealcount",setmealcount);


            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    /*
    *   获取会员报表信息(表格)
    * */

    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData() {
        try {
            Map<String, Object> map = reportService.getBusinessReportData();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    /*
    *   导出报表(提前顶一模版,导出数据)
    * */
    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request , HttpServletResponse response) {
        try {
            //创建容器,用于封装数据库查询数据
            Map<String, Object> map = reportService.getBusinessReportData();
            String reportDate = (String) map.get("reportDate");
            Integer todayNewMember = (Integer) map.get("todayNewMember");
            Integer totalMember = (Integer) map.get("totalMember");
            Integer thisWeekNewMember = (Integer) map.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) map.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) map.get("todayOrderNumber");
            Integer todayVisitsNumber = (Integer) map.get("todayVisitsNumber");
            Integer thisWeekOrderNumber = (Integer) map.get("thisWeekOrderNumber");
            Integer thisWeekVisitsNumber = (Integer) map.get("thisWeekVisitsNumber");
            Integer thisMonthOrderNumber = (Integer) map.get("thisMonthOrderNumber");
            Integer thisMonthVisitsNumber = (Integer) map.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) map.get("hotSetmeal");

            //创建模板对象
            //读取Excel文件(项目路径/template/reopt_template.xlsx),将数据存放在Excel文件中指定的单元格中(Cell)
            String path = request.getSession().getServletContext().getRealPath("/template") + File.separator + "report_template.xlsx";
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(path)));
            XSSFSheet sheet = workbook.getSheetAt(0);

            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);//日期

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            //热点套餐详情
            int rowCount = 12;
            for (Map map1 : hotSetmeal) {
                //套餐名称
                String name = (String) map1.get("name");
                Long setmeal_count = (Long) map1.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map1.get("proportion");
                //获取行,并赋值
                row = sheet.getRow(rowCount++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }

                //通过输出流进行文件下载
                ServletOutputStream out = response.getOutputStream();
                response.setContentType("application/vnd.ms-excel");
                response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
                workbook.write(out);

                out.flush();
                out.close();
                workbook.close();

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }
}
