package com.bhumi.paymentrouter.controller;

import java.util.List;

import com.bhumi.paymentrouter.dto.CreatePaymentRequest;
import com.bhumi.paymentrouter.entity.Payment;
import com.bhumi.paymentrouter.service.PaymentService;
import com.bhumi.paymentrouter.dto.UpdatePaymentStatusRequest;
import com.bhumi.paymentrouter.dto.PaymentStatsResponse;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public Payment createPayment(
            @RequestBody CreatePaymentRequest request) {

        return paymentService.createPayment(request);
    }

    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{id}")
    public Payment getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id);
    }

    @PutMapping("/{id}/status")
    public Payment updatePaymentStatus(
        @PathVariable Long id,
        @RequestBody UpdatePaymentStatusRequest request) {

    return paymentService.updatePaymentStatus(id, request);
    }

    @GetMapping("/stats")
    public PaymentStatsResponse getPaymentStats() {
        return paymentService.getPaymentStats();
    }

    @DeleteMapping("/{id}")
    public String deletePayment(@PathVariable Long id) {

        paymentService.deletePayment(id);

        return "Payment deleted successfully";
    }
}