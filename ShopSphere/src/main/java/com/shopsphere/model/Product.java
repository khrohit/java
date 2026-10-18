package com.shopsphere.model;

import java.math.BigDecimal;

public class Product {
    private long productId;
    private long categoryId;
    private String name;
    private String brand;
    private String description;
    private BigDecimal price;
    private BigDecimal discount;
    private int stock;
    private String imageUrl;
    private String categoryName;

    public long getProductId() { return productId; }
    public void setProductId(long productId) { this.productId = productId; }
    public long getCategoryId() { return categoryId; }
    public void setCategoryId(long categoryId) { this.categoryId = categoryId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public BigDecimal getSalePrice() {
        return price.subtract(price.multiply(discount).divide(new BigDecimal("100")));
    }
}
