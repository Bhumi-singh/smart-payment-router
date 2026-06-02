package com.bhumi.paymentrouter.kafka;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.bhumi.paymentrouter.entity.Payment;
import com.bhumi.paymentrouter.entity.PaymentStatus;
import com.bhumi.paymentrouter.repository.PaymentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bhumi.paymentrouter.kafka.DLQProducer;

@Service
public class PaymentConsumer {

    private final PaymentRepository paymentRepository;
    private final ObjectMapper objectMapper;
    private final DLQProducer dlqProducer;

    public PaymentConsumer(
        PaymentRepository paymentRepository,
        ObjectMapper objectMapper,
        DLQProducer dlqProducer) {

    this.paymentRepository = paymentRepository;
    this.objectMapper = objectMapper;
    this.dlqProducer = dlqProducer;
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

        Payment payment =
                paymentRepository.findById(event.getPaymentId())
                        .orElseThrow();

        boolean processed = false;
        int retryCount = 0;
        int maxRetries = 3;

        while (!processed && retryCount < maxRetries) {

            try {

                retryCount++;

                System.out.println(
                        "Attempt "
                                + retryCount
                                + " for payment "
                                + payment.getId());

                Thread.sleep(2000);

                boolean gatewaySuccess =
                        new Random().nextInt(100) < 70;

                if (!gatewaySuccess) {
                    throw new RuntimeException(
                            "Gateway timeout");
                }

                payment.setStatus(
                        PaymentStatus.SUCCESS);

                payment.setUpdatedAt(
                        LocalDateTime.now());

                paymentRepository.save(payment);

                System.out.println(
                        "Payment "
                                + payment.getId()
                                + " SUCCESS");

                processed = true;

            } catch (Exception e) {

                System.out.println(
                        "Retry "
                                + retryCount
                                + " failed");

                if (retryCount == maxRetries) {

                    payment.setStatus(
                            PaymentStatus.FAILED);

                    payment.setUpdatedAt(
                            LocalDateTime.now());

                    paymentRepository.save(payment);

                    System.out.println(
                            "Payment "
                                    + payment.getId()
                                    + " FAILED after "
                                    + maxRetries
                                    + " retries");
                }
            }
        }

    } catch (Exception e) {

        System.out.println("Processing failed.");
        dlqProducer.sendToDLQ(message);
        e.printStackTrace();
        }
    }
}