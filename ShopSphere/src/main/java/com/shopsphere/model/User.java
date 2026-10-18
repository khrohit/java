package com.shopsphere.model;

public class User {
    private final long userId;
    private final String name;
    private final String email;
    private final String mobile;
    private final String role;

    public User(long userId, String name, String email, String mobile, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.role = role;
    }

    public long getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getMobile() { return mobile; }
    public String getRole() { return role; }
    public boolean isAdmin() { return "ADMIN".equals(role); }
}
