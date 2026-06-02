package com.bhumi.paymentrouter.controller;

import com.bhumi.paymentrouter.dto.CacheStatusResponse;
import com.bhumi.paymentrouter.dto.DLQResponse;
import com.bhumi.paymentrouter.dto.DashboardResponse;
import com.bhumi.paymentrouter.dto.RecentPaymentResponse;
import com.bhumi.paymentrouter.service.PaymentService;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bhumi.paymentrouter.kafka.DLQConsumer;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    private final PaymentService paymentService;

    public DashboardController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public DashboardResponse getDashboard() {
        return paymentService.getDashboard();
    }

    @GetMapping("/recent")
    public List<RecentPaymentResponse> getRecentPayments() {
        return paymentService.getRecentPayments();
    }

    @GetMapping("/dlq-count")
    public Map<String, Integer> getDlqCount() {

        Map<String, Integer> response = new HashMap<>();
        response.put("count", DLQConsumer.dlqCount);

        return response;
    }

    @GetMapping("/cache")
    public CacheStatusResponse getCacheStatus() {

        CacheStatusResponse response =
            new CacheStatusResponse();

        response.setRedis("UP");

    return response;
    }
}