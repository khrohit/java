package com.shopsphere.dao;

import com.shopsphere.model.CartItem;
import com.shopsphere.model.Product;
import com.shopsphere.util.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {
    private static final String CART_SELECT = "SELECT p.product_id,p.category_id,p.name,p.brand,p.description,p.price,p.discount,p.stock,p.image_url,c.name category_name,ci.quantity FROM cart ci JOIN products p ON p.product_id=ci.product_id LEFT JOIN categories c ON c.category_id=p.category_id ";

    public List<CartItem> findByUser(long userId) throws SQLException {
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(CART_SELECT + "WHERE ci.user_id=? AND p.status=1 ORDER BY p.name")) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                List<CartItem> items = new ArrayList<>();
                while (rs.next()) items.add(new CartItem(map(rs), rs.getInt("quantity")));
                return items;
            }
        }
    }

    public void add(long userId, long productId, int quantity) throws SQLException {
        String sql = "INSERT INTO cart(user_id,product_id,quantity) SELECT ?,product_id,? FROM products WHERE product_id=? AND status=1 AND stock>=? ON DUPLICATE KEY UPDATE quantity=LEAST(quantity+VALUES(quantity), (SELECT stock FROM products WHERE product_id=VALUES(product_id)))";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, userId); ps.setInt(2, quantity); ps.setLong(3, productId); ps.setInt(4, quantity);
            if (ps.executeUpdate() == 0) throw new IllegalArgumentException("That product is unavailable or there is not enough stock.");
        }
    }

    public void update(long userId, long productId, int quantity) throws SQLException {
        if (quantity <= 0) { remove(userId, productId); return; }
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(
                "UPDATE cart ci JOIN products p ON p.product_id=ci.product_id SET ci.quantity=LEAST(?,p.stock) WHERE ci.user_id=? AND ci.product_id=? AND p.stock>0")) {
            ps.setInt(1, quantity); ps.setLong(2, userId); ps.setLong(3, productId); ps.executeUpdate();
        }
    }

    public void remove(long userId, long productId) throws SQLException {
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM cart WHERE user_id=? AND product_id=?")) {
            ps.setLong(1, userId); ps.setLong(2, productId); ps.executeUpdate();
        }
    }

    private Product map(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setProductId(rs.getLong("product_id")); p.setCategoryId(rs.getLong("category_id")); p.setName(rs.getString("name"));
        p.setBrand(rs.getString("brand")); p.setDescription(rs.getString("description")); p.setPrice(rs.getBigDecimal("price"));
        p.setDiscount(rs.getBigDecimal("discount")); p.setStock(rs.getInt("stock")); p.setImageUrl(rs.getString("image_url"));
        p.setCategoryName(rs.getString("category_name")); return p;
    }
}
