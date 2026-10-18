package com.shopsphere.dao;

import com.shopsphere.model.Product;
import com.shopsphere.util.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WishlistDAO {
    public List<Product> findByUser(long userId) throws SQLException {
        String sql = "SELECT p.*,c.name category_name FROM wishlist w JOIN products p ON p.product_id=w.product_id LEFT JOIN categories c ON c.category_id=p.category_id WHERE w.user_id=? AND p.status=1 ORDER BY w.created_at DESC";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Product> products = new ArrayList<>();
                while (rs.next()) {
                    Product p = new Product(); p.setProductId(rs.getLong("product_id")); p.setCategoryId(rs.getLong("category_id"));
                    p.setName(rs.getString("name")); p.setBrand(rs.getString("brand")); p.setDescription(rs.getString("description"));
                    p.setPrice(rs.getBigDecimal("price")); p.setDiscount(rs.getBigDecimal("discount")); p.setStock(rs.getInt("stock"));
                    p.setImageUrl(rs.getString("image_url")); p.setCategoryName(rs.getString("category_name")); products.add(p);
                }
                return products;
            }
        }
    }
    public void add(long userId, long productId) throws SQLException {
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(
                "INSERT IGNORE INTO wishlist(user_id,product_id) SELECT ?,product_id FROM products WHERE product_id=? AND status=1")) {
            ps.setLong(1, userId); ps.setLong(2, productId); ps.executeUpdate();
        }
    }
    public void remove(long userId, long productId) throws SQLException {
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM wishlist WHERE user_id=? AND product_id=?")) {
            ps.setLong(1, userId); ps.setLong(2, productId); ps.executeUpdate();
        }
    }
}
