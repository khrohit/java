package com.shopsphere.model;

import java.math.BigDecimal;
import java.sql.Date;

public class Coupon {
    private final long couponId;
    private final String code;
    private final String type;
    private final BigDecimal value;
    private final BigDecimal minimumOrder;
    private final boolean active;
    private final Date expiryDate;
    public Coupon(long couponId, String code, String type, BigDecimal value, BigDecimal minimumOrder, boolean active, Date expiryDate) {
        this.couponId = couponId; this.code = code; this.type = type; this.value = value; this.minimumOrder = minimumOrder; this.active = active; this.expiryDate = expiryDate;
    }
    public long getCouponId() { return couponId; }
    public String getCode() { return code; }
    public String getType() { return type; }
    public BigDecimal getValue() { return value; }
    public BigDecimal getMinimumOrder() { return minimumOrder; }
    public boolean isActive() { return active; }
    public Date getExpiryDate() { return expiryDate; }
}
