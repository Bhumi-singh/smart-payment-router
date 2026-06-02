package com.bhumi.paymentrouter.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class DLQConsumer {

    @KafkaListener(
            topics = "payments-dlq",
            groupId = "dlq-group")
    public void consume(String message) {

        System.out.println(
                "DLQ MESSAGE RECEIVED -> "
                        + message);
    }
}