package com.quick.controller.admin;

import com.quick.result.Result;
import com.quick.service.ReportService;
import com.quick.vo.OrderReportVO;
import com.quick.vo.SalesTop10ReportVO;
import com.quick.vo.TurnoverReportVO;
import com.quick.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

/*
报表统计
 */
@RestController
@RequestMapping("/admin/report")
@Slf4j
@Api(tags = "报表统计相关接口")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/turnoverStatistics")
    @ApiOperation("营业额数据统计")
    public Result<TurnoverReportVO> turnoverStatistics
            (@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
             @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {

        log.info("查询营业额数据：{} ~ {}", begin, end);
        return Result.success(reportService.getTurnoverStatistics(begin, end));
    }
    /*
    用户统计
     */
    @GetMapping("/userStatistics")
    @ApiOperation("用户数据统计")
    public Result<UserReportVO> userStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end
            ) {
        log.info("查询用户数据：{} ~ {}", begin, end);
        return Result.success(reportService.getUserStatistics(begin, end));
    }
    /*
    订单统计
     */
    @GetMapping("/ordersStatistics")
    @ApiOperation("订单数据统计")
    public Result<OrderReportVO> ordersStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end
    ){
        log.info("查询订单数据：{} ~ {}", begin, end);
        return Result.success(reportService.getOrderStatistics(begin, end));
    }
    /*
    销量排名
     */
    @GetMapping("/top10")
    @ApiOperation("销量排名top10")
    public Result<SalesTop10ReportVO> top10(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end
    ){
        log.info("查询销量排名：{} ~ {}", begin, end);
        return Result.success(reportService.getSalesTop10(begin, end));
    }
    /*
    导出数据
     */
    @GetMapping("/export")
    @ApiOperation("导出数据")
    public void export(HttpServletResponse response){
        log.info("导出数据");
        reportService.exportBusinessData(response);
    }
}
