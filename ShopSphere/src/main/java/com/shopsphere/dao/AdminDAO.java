package com.shopsphere.dao;

import com.shopsphere.util.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class AdminDAO {
    public Map<String, Long> dashboardCounts() throws SQLException {
        Map<String, Long> counts = new LinkedHashMap<>();
        try (Connection c = Database.getConnection()) {
            counts.put("products", count(c, "SELECT COUNT(*) FROM products WHERE status=1"));
            counts.put("customers", count(c, "SELECT COUNT(*) FROM users WHERE role='CUSTOMER' AND status=1"));
            counts.put("orders", count(c, "SELECT COUNT(*) FROM orders"));
            counts.put("lowStock", count(c, "SELECT COUNT(*) FROM products WHERE status=1 AND stock<5"));
        }
        return counts;
    }

    private long count(Connection c, String sql) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            rs.next(); return rs.getLong(1);
        }
    }
}
