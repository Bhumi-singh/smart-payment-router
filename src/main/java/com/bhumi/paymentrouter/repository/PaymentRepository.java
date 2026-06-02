package com.bhumi.paymentrouter.repository;

import com.bhumi.paymentrouter.entity.Payment;
import com.bhumi.paymentrouter.entity.PaymentStatus;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {
        long countByStatus(PaymentStatus status);
        List<Payment> findAllByOrderByCreatedAtDesc(Pageable pageable);
        List<Payment> findByOrderId(String orderId);
}