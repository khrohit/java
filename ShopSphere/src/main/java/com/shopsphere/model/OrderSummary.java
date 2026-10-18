package com.shopsphere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class OrderSummary {
    private final long orderId;
    private final BigDecimal totalAmount;
    private final BigDecimal discount;
    private final String orderStatus;
    private final String paymentStatus;
    private final Timestamp createdAt;

    public OrderSummary(long orderId, BigDecimal totalAmount, BigDecimal discount,
                        String orderStatus, String paymentStatus, Timestamp createdAt) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.discount = discount;
        this.orderStatus = orderStatus;
        this.paymentStatus = paymentStatus;
        this.createdAt = createdAt;
    }

    public long getOrderId() { return orderId; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public BigDecimal getDiscount() { return discount; }
    public String getOrderStatus() { return orderStatus; }
    public String getPaymentStatus() { return paymentStatus; }
    public Timestamp getCreatedAt() { return createdAt; }
}
