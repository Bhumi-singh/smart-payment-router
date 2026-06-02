package com.bhumi.paymentrouter.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class DLQProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public DLQProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendToDLQ(String message) {

        kafkaTemplate.send(
                "payments-dlq",
                message);

        System.out.println(
                "Sent to DLQ -> "
                        + message);
    }
}