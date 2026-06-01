package com.bhumi.paymentrouter.service;

import java.util.List;

import com.bhumi.paymentrouter.exception.PaymentNotFoundException;
import com.bhumi.paymentrouter.dto.CreatePaymentRequest;
import com.bhumi.paymentrouter.entity.Payment;
import com.bhumi.paymentrouter.entity.PaymentStatus;
import com.bhumi.paymentrouter.gateway.GatewayRouter;
import com.bhumi.paymentrouter.repository.PaymentRepository;
import com.bhumi.paymentrouter.dto.UpdatePaymentStatusRequest;
import com.bhumi.paymentrouter.dto.PaymentStatsResponse;
import com.bhumi.paymentrouter.kafka.PaymentProducer;
import com.bhumi.paymentrouter.kafka.PaymentEvent;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final GatewayRouter gatewayRouter;
    private final PaymentProducer paymentProducer;

    public PaymentService(
        PaymentRepository paymentRepository,
        GatewayRouter gatewayRouter,
        PaymentProducer paymentProducer) {

    this.paymentRepository = paymentRepository;
    this.gatewayRouter = gatewayRouter;
    this.paymentProducer = paymentProducer;
    }

    public Payment createPayment(CreatePaymentRequest request) {

        Payment payment = new Payment();

        payment.setOrderId(request.getOrderId());
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency());

        payment.setStatus(PaymentStatus.PENDING);

        payment.setGateway(
                gatewayRouter.selectGateway(request.getAmount())
        );

        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);

        paymentProducer.publish(
            new PaymentEvent(
                savedPayment.getId(),
                savedPayment.getOrderId()
            )
        );

        return savedPayment;
    }
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
            .orElseThrow(() -> new PaymentNotFoundException(id));
    }
    public Payment updatePaymentStatus(
        Long id,
        UpdatePaymentStatusRequest request) {

        Payment payment = paymentRepository.findById(id)
            .orElseThrow(() -> new PaymentNotFoundException(id));

        payment.setStatus(request.getStatus());
        payment.setUpdatedAt(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    public PaymentStatsResponse getPaymentStats() {

    List<Payment> payments = paymentRepository.findAll();

    PaymentStatsResponse stats = new PaymentStatsResponse();

    stats.setTotalPayments(payments.size());

    stats.setSuccessPayments(
            payments.stream()
                    .filter(p -> p.getStatus() == PaymentStatus.SUCCESS)
                    .count()
    );

    stats.setFailedPayments(
            payments.stream()
                    .filter(p -> p.getStatus() == PaymentStatus.FAILED)
                    .count()
    );

    stats.setPendingPayments(
            payments.stream()
                    .filter(p -> p.getStatus() == PaymentStatus.PENDING)
                    .count()
    );

        return stats;
    }

    public void deletePayment(Long id) {

        Payment payment = paymentRepository.findById(id)
            .orElseThrow(() -> new PaymentNotFoundException(id));

        paymentRepository.delete(payment);
    }
}