package com.bhumi.paymentrouter.service;

import java.util.List;

import com.bhumi.paymentrouter.exception.PaymentNotFoundException;
import com.bhumi.paymentrouter.dto.CacheStatusResponse;
import com.bhumi.paymentrouter.dto.CreatePaymentRequest;
import com.bhumi.paymentrouter.entity.Payment;
import com.bhumi.paymentrouter.entity.PaymentStatus;
import com.bhumi.paymentrouter.gateway.GatewayRouter;
import com.bhumi.paymentrouter.repository.PaymentRepository;
import com.bhumi.paymentrouter.dto.UpdatePaymentStatusRequest;
import com.bhumi.paymentrouter.dto.PaymentStatsResponse;
import com.bhumi.paymentrouter.kafka.PaymentProducer;
import com.bhumi.paymentrouter.kafka.PaymentEvent;
import org.springframework.data.redis.core.RedisTemplate;
import com.bhumi.paymentrouter.dto.DashboardResponse;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;

import com.bhumi.paymentrouter.dto.RecentPaymentResponse;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final GatewayRouter gatewayRouter;
    private final PaymentProducer paymentProducer;
    private final RedisTemplate<String, Object> redisTemplate;
    private int dlqCount = 0;

    public PaymentService(
        PaymentRepository paymentRepository,
        GatewayRouter gatewayRouter,
        PaymentProducer paymentProducer,
        RedisTemplate<String, Object> redisTemplate) {

    this.paymentRepository = paymentRepository;
    this.gatewayRouter = gatewayRouter;
    this.paymentProducer = paymentProducer;
    this.redisTemplate = redisTemplate;
    }

    public List<Payment> findByOrderId(String orderId) {

        return paymentRepository.findByOrderId(orderId);
    }

    public void incrementDlqCount() {
        dlqCount++;
    }   

    public int getDlqCount() {
        return dlqCount;
    }

    public DashboardResponse getDashboard() {

    DashboardResponse response =
            new DashboardResponse();

    long total =
            paymentRepository.count();

    long success =
            paymentRepository.countByStatus(
                    PaymentStatus.SUCCESS);

    long failed =
            paymentRepository.countByStatus(
                    PaymentStatus.FAILED);

    long pending =
            paymentRepository.countByStatus(
                    PaymentStatus.PENDING);

    double successRate = 0;

    if (total > 0) {
        successRate =
                (success * 100.0) / total;
    }

    response.setTotalPayments(total);
    response.setSuccessPayments(success);
    response.setFailedPayments(failed);
    response.setPendingPayments(pending);
    response.setSuccessRate(Math.round(successRate * 100.0) / 100.0);

    return response;
    }

    public List<RecentPaymentResponse> getRecentPayments() {

    return paymentRepository
            .findAllByOrderByCreatedAtDesc(
                    PageRequest.of(0, 10))
            .stream()
            .map(payment -> {

                RecentPaymentResponse response =
                        new RecentPaymentResponse();

                response.setId(payment.getId());
                response.setOrderId(
                        payment.getOrderId());

                response.setAmount(
                        payment.getAmount());

                response.setStatus(
                        payment.getStatus().name());

                return response;

            }).collect(Collectors.toList());
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

        String key = "payment:" + id;

        Payment cachedPayment =
            (Payment) redisTemplate.opsForValue().get(key);
            if (cachedPayment != null) {
            System.out.println("FROM REDIS");
            return cachedPayment;
        }

        System.out.println("FROM POSTGRES");

        Payment payment =
            paymentRepository.findById(id)
                    .orElseThrow(() ->
                            new PaymentNotFoundException(id));

        redisTemplate.opsForValue().set(key, payment);

        return payment;
    }

    public CacheStatusResponse getCacheStatus() {

        CacheStatusResponse response =
            new CacheStatusResponse();

        response.setRedis("UP");

        return response;
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