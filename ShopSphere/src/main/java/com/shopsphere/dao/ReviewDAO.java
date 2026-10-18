package com.shopsphere.dao;

import com.shopsphere.model.Review;
import com.shopsphere.util.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {
    public List<Review> findByProduct(long productId) throws SQLException {
        String sql = "SELECT u.name,r.rating,r.review_text,r.created_at FROM reviews r JOIN users u ON u.user_id=r.user_id WHERE r.product_id=? ORDER BY r.created_at DESC";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Review> reviews = new ArrayList<>();
                while (rs.next()) reviews.add(new Review(rs.getString("name"), rs.getInt("rating"), rs.getString("review_text"), rs.getTimestamp("created_at")));
                return reviews;
            }
        }
    }
    public void add(long userId, long productId, int rating, String text) throws SQLException {
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("INSERT INTO reviews(user_id,product_id,rating,review_text) SELECT ?,product_id,?,? FROM products WHERE product_id=? AND status=1")) {
            ps.setLong(1, userId); ps.setInt(2, rating); ps.setString(3, text); ps.setLong(4, productId);
            if (ps.executeUpdate() == 0) throw new IllegalArgumentException("That product is not available.");
        }
    }
}
