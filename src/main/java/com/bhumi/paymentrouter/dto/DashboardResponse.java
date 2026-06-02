package com.bhumi.paymentrouter.dto;

public class DashboardResponse {

    private long totalPayments;
    private long successPayments;
    private long failedPayments;
    private long pendingPayments;
    private double successRate;

    public long getTotalPayments() {
        return totalPayments;
    }

    public void setTotalPayments(long totalPayments) {
        this.totalPayments = totalPayments;
    }

    public long getSuccessPayments() {
        return successPayments;
    }

    public void setSuccessPayments(long successPayments) {
        this.successPayments = successPayments;
    }

    public long getFailedPayments() {
        return failedPayments;
    }

    public void setFailedPayments(long failedPayments) {
        this.failedPayments = failedPayments;
    }

    public long getPendingPayments() {
        return pendingPayments;
    }

    public void setPendingPayments(long pendingPayments) {
        this.pendingPayments = pendingPayments;
    }

    public double getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(double successRate) {
        this.successRate = successRate;
    }
}