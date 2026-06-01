package com.bhumi.paymentrouter.dto;

import com.bhumi.paymentrouter.entity.PaymentStatus;

public class UpdatePaymentStatusRequest {

    private PaymentStatus status;

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}