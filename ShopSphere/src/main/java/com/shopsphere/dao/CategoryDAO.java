package com.shopsphere.dao;

import com.shopsphere.model.Category;
import com.shopsphere.util.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    public List<Category> findAll() throws SQLException {
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT category_id,name,description,status FROM categories ORDER BY name"); ResultSet rs = ps.executeQuery()) {
            List<Category> categories = new ArrayList<>();
            while (rs.next()) categories.add(new Category(rs.getLong("category_id"), rs.getString("name"), rs.getString("description"), rs.getBoolean("status")));
            return categories;
        }
    }
    public void save(long id, String name, String description) throws SQLException {
        String sql = id == 0 ? "INSERT INTO categories(name,description,status) VALUES(?,?,1)" : "UPDATE categories SET name=?,description=?,status=1 WHERE category_id=?";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name); ps.setString(2, description); if (id != 0) ps.setLong(3, id); ps.executeUpdate();
        }
    }
    public void archive(long id) throws SQLException {
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("UPDATE categories SET status=0 WHERE category_id=?")) {
            ps.setLong(1, id); ps.executeUpdate();
        }
    }
}
