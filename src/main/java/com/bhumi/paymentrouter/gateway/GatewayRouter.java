package com.bhumi.paymentrouter.gateway;

import com.bhumi.paymentrouter.entity.PaymentGateway;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class GatewayRouter {

    public PaymentGateway selectGateway(BigDecimal amount) {

        if (amount.compareTo(new BigDecimal("5000")) < 0) {
            return PaymentGateway.RAZORPAY;
        }

        return PaymentGateway.STRIPE;
    }
}