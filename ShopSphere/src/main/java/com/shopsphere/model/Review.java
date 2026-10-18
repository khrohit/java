package com.shopsphere.model;

import java.sql.Timestamp;

public class Review {
    private final String customerName;
    private final int rating;
    private final String text;
    private final Timestamp createdAt;
    public Review(String customerName, int rating, String text, Timestamp createdAt) {
        this.customerName = customerName; this.rating = rating; this.text = text; this.createdAt = createdAt;
    }
    public String getCustomerName() { return customerName; }
    public int getRating() { return rating; }
    public String getText() { return text; }
    public Timestamp getCreatedAt() { return createdAt; }
}
