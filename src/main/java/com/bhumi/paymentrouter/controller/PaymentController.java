package com.bhumi.paymentrouter.controller;

import com.bhumi.paymentrouter.dto.CreatePaymentRequest;
import com.bhumi.paymentrouter.entity.Payment;
import com.bhumi.paymentrouter.service.PaymentService;
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
}