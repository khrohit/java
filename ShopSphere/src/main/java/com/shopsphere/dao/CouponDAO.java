package com.shopsphere.dao;

import com.shopsphere.model.Coupon;
import com.shopsphere.util.Database;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CouponDAO {
    public List<Coupon> findAll() throws SQLException {
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT coupon_id,code,discount_type,discount_value,minimum_order,status,expiry_date FROM coupons ORDER BY code"); ResultSet rs = ps.executeQuery()) {
            List<Coupon> coupons = new ArrayList<>();
            while (rs.next()) coupons.add(new Coupon(rs.getLong("coupon_id"), rs.getString("code"), rs.getString("discount_type"), rs.getBigDecimal("discount_value"), rs.getBigDecimal("minimum_order"), rs.getBoolean("status"), rs.getDate("expiry_date")));
            return coupons;
        }
    }
    public void create(String code, String type, BigDecimal value, BigDecimal minimum, BigDecimal maximum, Date expiry) throws SQLException {
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("INSERT INTO coupons(code,discount_type,discount_value,minimum_order,maximum_discount,expiry_date,status) VALUES(?,?,?,?,?,?,1)")) {
            ps.setString(1, code.toUpperCase()); ps.setString(2, type); ps.setBigDecimal(3, value); ps.setBigDecimal(4, minimum); ps.setBigDecimal(5, maximum); ps.setDate(6, expiry); ps.executeUpdate();
        }
    }
    public void archive(long id) throws SQLException {
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("UPDATE coupons SET status=0 WHERE coupon_id=?")) {
            ps.setLong(1, id); ps.executeUpdate();
        }
    }
}
