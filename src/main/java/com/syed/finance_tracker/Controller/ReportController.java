package com.syed.finance_tracker.Controller;

import com.syed.finance_tracker.Dto.MonthlyReportResponse;
import com.syed.finance_tracker.Service.ReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/reports/monthly")
    public MonthlyReportResponse getMonthlyReport(
            @RequestParam String month) {

        return reportService.getMonthlyReport(month);
    }
}