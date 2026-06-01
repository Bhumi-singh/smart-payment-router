package com.bhumi.paymentrouter.repository;

import com.bhumi.paymentrouter.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {
}