package com.bhumi.paymentrouter.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Payment {

    private Long id;

    private String orderId;

    private BigDecimal amount;

    private String currency;

    private PaymentStatus status;

    private PaymentGateway gateway;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}