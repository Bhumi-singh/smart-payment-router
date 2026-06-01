package com.bhumi.paymentrouter.kafka;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.bhumi.paymentrouter.entity.Payment;
import com.bhumi.paymentrouter.entity.PaymentStatus;
import com.bhumi.paymentrouter.repository.PaymentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PaymentConsumer {

    private final PaymentRepository paymentRepository;
    private final ObjectMapper objectMapper;

    public PaymentConsumer(
            PaymentRepository paymentRepository,
            ObjectMapper objectMapper) {

        this.paymentRepository = paymentRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "payments",
            groupId = "payment-group")
    public void consume(String message) {

        try {

            PaymentEvent event =
                    objectMapper.readValue(message, PaymentEvent.class);

            System.out.println(
                    "Processing payment : "
                            + event.getPaymentId());

            Thread.sleep(3000);

            Payment payment =
                    paymentRepository.findById(event.getPaymentId())
                            .orElseThrow();

            boolean success =
                    new Random().nextBoolean();

            if (success) {
                payment.setStatus(PaymentStatus.SUCCESS);
            } else {
                payment.setStatus(PaymentStatus.FAILED);
            }

            payment.setUpdatedAt(LocalDateTime.now());

            paymentRepository.save(payment);

            System.out.println(
                    "Payment "
                            + payment.getId()
                            + " -> "
                            + payment.getStatus());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}