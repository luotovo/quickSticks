package com.quick.service;

import com.quick.vo.OrderReportVO;
import com.quick.vo.SalesTop10ReportVO;
import com.quick.vo.TurnoverReportVO;
import com.quick.vo.UserReportVO;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

public interface ReportService {
    /*
    * 统计时间段营业额数据
     */
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);

    /*
    * 统计时间段用户数据
     */
    UserReportVO getUserStatistics(LocalDate begin, LocalDate end);

    /*
    * 统计时间段订单数据
     */
    OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end);

    /*
    统计时间段销量排名top10
     */
    SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end);

    /*
    导出营业数据报表
     */
    void exportBusinessData(HttpServletResponse response);
}
