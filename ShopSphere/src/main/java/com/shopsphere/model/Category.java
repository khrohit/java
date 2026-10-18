package com.shopsphere.model;

public class Category {
    private final long categoryId;
    private final String name;
    private final String description;
    private final boolean active;
    public Category(long categoryId, String name, String description, boolean active) {
        this.categoryId = categoryId; this.name = name; this.description = description; this.active = active;
    }
    public long getCategoryId() { return categoryId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isActive() { return active; }
}
