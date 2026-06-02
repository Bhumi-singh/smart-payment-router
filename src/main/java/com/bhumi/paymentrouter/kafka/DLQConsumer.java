package com.bhumi.paymentrouter.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.bhumi.paymentrouter.service.PaymentService;

@Service
public class DLQConsumer {
    private final PaymentService paymentService;
    public static int dlqCount = 0;

    public DLQConsumer(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    @KafkaListener(
            topics = "payments-dlq",
            groupId = "dlq-group")
    public void consume(String message) {

        paymentService.incrementDlqCount();

        System.out.println(
                "DLQ MESSAGE RECEIVED -> "
                        + message);

        
    }
}