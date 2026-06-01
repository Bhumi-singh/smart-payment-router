package com.bhumi.paymentrouter.kafka;

public class PaymentEvent {

    private Long paymentId;
    private String orderId;

    public PaymentEvent() {
    }

    public PaymentEvent(Long paymentId, String orderId) {
        this.paymentId = paymentId;
        this.orderId = orderId;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}