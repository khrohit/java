package com.shopsphere.dao;

import com.shopsphere.model.Product;
import com.shopsphere.util.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private static final String SELECT = "SELECT p.*, c.name AS category_name FROM products p LEFT JOIN categories c ON c.category_id=p.category_id ";

    public List<Product> findAll(String search, Long categoryId, String sort) throws SQLException {
        StringBuilder sql = new StringBuilder(SELECT).append("WHERE p.status=1 ");
        if (search != null && !search.isBlank()) sql.append("AND (p.name LIKE ? OR p.brand LIKE ? OR p.description LIKE ?) ");
        if (categoryId != null && categoryId > 0) sql.append("AND p.category_id=? ");
        sql.append(switch (sort == null ? "" : sort) {
            case "price-low" -> "ORDER BY p.price ASC";
            case "price-high" -> "ORDER BY p.price DESC";
            case "name" -> "ORDER BY p.name ASC";
            default -> "ORDER BY p.created_at DESC";
        });
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql.toString())) {
            int i = 1;
            if (search != null && !search.isBlank()) {
                String term = "%" + search.trim() + "%";
                ps.setString(i++, term); ps.setString(i++, term); ps.setString(i++, term);
            }
            if (categoryId != null && categoryId > 0) ps.setLong(i, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Product> products = new ArrayList<>();
                while (rs.next()) products.add(map(rs));
                return products;
            }
        }
    }

    public List<Product> findPage(String search, Long categoryId, String sort, int limit, int offset) throws SQLException {
        StringBuilder sql = new StringBuilder(SELECT).append("WHERE p.status=1 ");
        if (search != null && !search.isBlank()) sql.append("AND (p.name LIKE ? OR p.brand LIKE ? OR p.description LIKE ?) ");
        if (categoryId != null && categoryId > 0) sql.append("AND p.category_id=? ");
        sql.append(switch (sort == null ? "" : sort) {
            case "price-low" -> "ORDER BY p.price ASC";
            case "price-high" -> "ORDER BY p.price DESC";
            case "name" -> "ORDER BY p.name ASC";
            default -> "ORDER BY p.created_at DESC";
        }).append(" LIMIT ? OFFSET ?");

        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql.toString())) {
            int i = bindFilters(ps, search, categoryId);
            ps.setInt(i++, Math.max(1, Math.min(limit, 48)));
            ps.setInt(i, Math.max(0, offset));
            try (ResultSet rs = ps.executeQuery()) {
                List<Product> products = new ArrayList<>();
                while (rs.next()) products.add(map(rs));
                return products;
            }
        }
    }

    public int countMatching(String search, Long categoryId) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM products p WHERE p.status=1 ");
        if (search != null && !search.isBlank()) sql.append("AND (p.name LIKE ? OR p.brand LIKE ? OR p.description LIKE ?) ");
        if (categoryId != null && categoryId > 0) sql.append("AND p.category_id=? ");
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql.toString())) {
            bindFilters(ps, search, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    private int bindFilters(PreparedStatement ps, String search, Long categoryId) throws SQLException {
        int i = 1;
        if (search != null && !search.isBlank()) {
            String term = "%" + search.trim() + "%";
            ps.setString(i++, term);
            ps.setString(i++, term);
            ps.setString(i++, term);
        }
        if (categoryId != null && categoryId > 0) ps.setLong(i++, categoryId);
        return i;
    }

    public List<Product> findFeatured() throws SQLException {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT + "WHERE p.status=1 ORDER BY p.discount DESC, p.created_at DESC LIMIT 8");
             ResultSet rs = ps.executeQuery()) {
            List<Product> products = new ArrayList<>();
            while (rs.next()) products.add(map(rs));
            return products;
        }
    }

    public Product findById(long id) throws SQLException {
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(SELECT + "WHERE p.product_id=? AND p.status=1")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? map(rs) : null; }
        }
    }

    public List<String[]> findCategories() throws SQLException {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT category_id,name FROM categories WHERE status=1 ORDER BY name");
             ResultSet rs = ps.executeQuery()) {
            List<String[]> categories = new ArrayList<>();
            while (rs.next()) categories.add(new String[]{rs.getString(1), rs.getString(2)});
            return categories;
        }
    }

    public List<Product> findAllForAdmin() throws SQLException {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT + "ORDER BY p.created_at DESC");
             ResultSet rs = ps.executeQuery()) {
            List<Product> products = new ArrayList<>();
            while (rs.next()) products.add(map(rs));
            return products;
        }
    }

    public void save(Product p) throws SQLException {
        String sql = p.getProductId() == 0
                ? "INSERT INTO products(category_id,name,brand,description,price,discount,stock,image_url,status) VALUES(?,?,?,?,?,?,?,?,1)"
                : "UPDATE products SET category_id=?,name=?,brand=?,description=?,price=?,discount=?,stock=?,image_url=?,status=1 WHERE product_id=?";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, p.getCategoryId()); ps.setString(2, p.getName()); ps.setString(3, p.getBrand());
            ps.setString(4, p.getDescription()); ps.setBigDecimal(5, p.getPrice()); ps.setBigDecimal(6, p.getDiscount());
            ps.setInt(7, p.getStock()); ps.setString(8, p.getImageUrl());
            if (p.getProductId() != 0) ps.setLong(9, p.getProductId());
            ps.executeUpdate();
        }
    }

    public void archive(long id) throws SQLException {
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("UPDATE products SET status=0 WHERE product_id=?")) {
            ps.setLong(1, id); ps.executeUpdate();
        }
    }

    public int getStock(long id) throws SQLException {
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT stock FROM products WHERE product_id=? AND status=1")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getInt(1) : -1; }
        }
    }

    public boolean setStock(long id, int stock) throws SQLException {
        if (stock < 0) return false;
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("UPDATE products SET stock=? WHERE product_id=? AND status=1")) {
            ps.setInt(1, stock); ps.setLong(2, id); return ps.executeUpdate() == 1;
        }
    }

    private Product map(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setProductId(rs.getLong("product_id")); p.setCategoryId(rs.getLong("category_id"));
        p.setName(rs.getString("name")); p.setBrand(rs.getString("brand")); p.setDescription(rs.getString("description"));
        p.setPrice(rs.getBigDecimal("price")); p.setDiscount(rs.getBigDecimal("discount")); p.setStock(rs.getInt("stock"));
        p.setImageUrl(rs.getString("image_url")); p.setCategoryName(rs.getString("category_name"));
        return p;
    }
}
