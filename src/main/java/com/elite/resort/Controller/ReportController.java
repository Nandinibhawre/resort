package com.elite.resort.Controller;

import com.elite.resort.DTO.ReportStatsDTO;
import com.elite.resort.Services.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/dashboard")
    public ReportStatsDTO getDashboardStats() {
        return reportService.getDashboardStats();
    }
}