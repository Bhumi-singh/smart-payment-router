package com.bhumi.paymentrouter.service;

import com.bhumi.paymentrouter.dto.CreatePaymentRequest;
import com.bhumi.paymentrouter.entity.Payment;
import com.bhumi.paymentrouter.entity.PaymentGateway;
import com.bhumi.paymentrouter.entity.PaymentStatus;
import com.bhumi.paymentrouter.gateway.GatewayRouter;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PaymentService {

    private final AtomicLong counter = new AtomicLong(1);

    private final GatewayRouter gatewayRouter;

    public PaymentService(GatewayRouter gatewayRouter) {
    this.gatewayRouter = gatewayRouter;
}

    public Payment createPayment(CreatePaymentRequest request) {

        Payment payment = new Payment();

        payment.setId(counter.getAndIncrement());
        payment.setOrderId(request.getOrderId());
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency());

        payment.setStatus(PaymentStatus.PENDING);
        payment.setGateway(
            gatewayRouter.selectGateway(request.getAmount())
        );
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        return payment;
    }
}