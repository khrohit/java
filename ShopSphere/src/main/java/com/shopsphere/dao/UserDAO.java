package com.shopsphere.dao;

import com.shopsphere.model.User;
import com.shopsphere.util.Database;
import java.sql.*;

public class UserDAO {
    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT user_id,name,email,password_hash,mobile,role FROM users WHERE email=? AND status=1";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? new User(rs.getLong("user_id"), rs.getString("name"), rs.getString("email"), rs.getString("mobile"), rs.getString("role")) : null;
            }
        }
    }

    public String findPasswordHash(String email) throws SQLException {
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT password_hash FROM users WHERE email=? AND status=1")) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getString(1) : null; }
        }
    }

    public void create(String name, String email, String mobile, String hash) throws SQLException {
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(
                "INSERT INTO users(name,email,mobile,password_hash,role,status) VALUES(?,?,?,?, 'CUSTOMER',1)")) {
            ps.setString(1, name); ps.setString(2, email); ps.setString(3, mobile); ps.setString(4, hash); ps.executeUpdate();
        }
    }

    public void updateProfile(long userId, String name, String mobile) throws SQLException {
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("UPDATE users SET name=?,mobile=? WHERE user_id=?")) {
            ps.setString(1, name); ps.setString(2, mobile); ps.setLong(3, userId); ps.executeUpdate();
        }
    }

    public java.util.List<User> findCustomers() throws SQLException {
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT user_id,name,email,mobile,role FROM users WHERE role='CUSTOMER' ORDER BY created_at DESC"); ResultSet rs = ps.executeQuery()) {
            java.util.List<User> customers = new java.util.ArrayList<>();
            while (rs.next()) customers.add(new User(rs.getLong("user_id"), rs.getString("name"), rs.getString("email"), rs.getString("mobile"), rs.getString("role")));
            return customers;
        }
    }
}
