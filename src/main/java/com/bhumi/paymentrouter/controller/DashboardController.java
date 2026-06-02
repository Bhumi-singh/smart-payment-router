package com.bhumi.paymentrouter.controller;

import com.bhumi.paymentrouter.dto.DashboardResponse;
import com.bhumi.paymentrouter.dto.RecentPaymentResponse;
import com.bhumi.paymentrouter.service.PaymentService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class DashboardController {

    private final PaymentService paymentService;

    public DashboardController(
            PaymentService paymentService) {

        this.paymentService = paymentService;
    }

    @GetMapping("/dashboard")
    public DashboardResponse dashboard() {

        return paymentService.getDashboard();
    }

    @GetMapping("/dashboard/recent")
    public List<RecentPaymentResponse> recentPayments() {

        return paymentService.getRecentPayments();
    }
}