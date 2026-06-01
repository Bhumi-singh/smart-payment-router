package com.bhumi.paymentrouter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PaymentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handlePaymentNotFound(
            PaymentNotFoundException ex) {

        return Map.of(
                "message", ex.getMessage(),
                "timestamp", LocalDateTime.now()
        );
    }
}