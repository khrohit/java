package com.shopsphere.dao;

import com.shopsphere.model.OrderSummary;
import com.shopsphere.util.Database;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderDAO {
    private record Line(long productId, String name, BigDecimal price, int quantity, int stock) { }
    private record Coupon(long id, String type, BigDecimal value, BigDecimal minimum, BigDecimal maximum) { }

    public long placeOrder(long userId, long addressId, String couponCode) throws SQLException {
        try (Connection c = Database.getConnection()) {
            c.setAutoCommit(false);
            try {
                List<Line> lines = lockCart(c, userId);
                if (lines.isEmpty()) throw new IllegalStateException("Your cart is empty.");
                BigDecimal subtotal = lines.stream().map(line -> line.price().multiply(BigDecimal.valueOf(line.quantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                Coupon coupon = findCoupon(c, couponCode, subtotal);
                BigDecimal discount = calculateDiscount(coupon, subtotal);
                BigDecimal total = subtotal.subtract(discount).max(BigDecimal.ZERO);
                long orderId = insertOrder(c, userId, addressId, total, discount);
                insertItemsAndReduceStock(c, orderId, lines);
                insertPayment(c, orderId, total);
                if (coupon != null) insertCouponUsage(c, coupon.id(), userId, orderId);
                try (PreparedStatement clear = c.prepareStatement("DELETE FROM cart WHERE user_id=?")) {
                    clear.setLong(1, userId); clear.executeUpdate();
                }
                c.commit();
                return orderId;
            } catch (SQLException | RuntimeException exception) {
                c.rollback();
                throw exception;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public List<OrderSummary> findByUser(long userId) throws SQLException {
        String sql = "SELECT order_id,total_amount,discount,order_status,payment_status,created_at FROM orders WHERE user_id=? ORDER BY created_at DESC";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                List<OrderSummary> orders = new ArrayList<>();
                while (rs.next()) orders.add(new OrderSummary(rs.getLong("order_id"), rs.getBigDecimal("total_amount"),
                        rs.getBigDecimal("discount"), rs.getString("order_status"), rs.getString("payment_status"), rs.getTimestamp("created_at")));
                return orders;
            }
        }
    }

    public List<OrderSummary> findAll() throws SQLException {
        String sql = "SELECT order_id,total_amount,discount,order_status,payment_status,created_at FROM orders ORDER BY created_at DESC LIMIT 100";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            List<OrderSummary> orders = new ArrayList<>();
            while (rs.next()) orders.add(new OrderSummary(rs.getLong("order_id"), rs.getBigDecimal("total_amount"),
                    rs.getBigDecimal("discount"), rs.getString("order_status"), rs.getString("payment_status"), rs.getTimestamp("created_at")));
            return orders;
        }
    }

    public void updateStatus(long orderId, String status) throws SQLException {
        if (!List.of("PLACED", "PROCESSING", "SHIPPED", "DELIVERED", "CANCELLED").contains(status))
            throw new IllegalArgumentException("Unsupported order status.");
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("UPDATE orders SET order_status=? WHERE order_id=?")) {
            ps.setString(1, status); ps.setLong(2, orderId); ps.executeUpdate();
        }
    }

    private List<Line> lockCart(Connection c, long userId) throws SQLException {
        String sql = "SELECT p.product_id,p.name,p.price,p.discount,p.stock,ci.quantity FROM cart ci JOIN products p ON p.product_id=ci.product_id WHERE ci.user_id=? AND p.status=1 FOR UPDATE";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Line> lines = new ArrayList<>();
                while (rs.next()) {
                    int quantity = rs.getInt("quantity");
                    int stock = rs.getInt("stock");
                    if (quantity > stock) throw new IllegalStateException("There is not enough stock for " + rs.getString("name") + ".");
                    BigDecimal price = rs.getBigDecimal("price");
                    BigDecimal percent = rs.getBigDecimal("discount");
                    BigDecimal salePrice = price.subtract(price.multiply(percent).divide(new BigDecimal("100")));
                    lines.add(new Line(rs.getLong("product_id"), rs.getString("name"), salePrice, quantity, stock));
                }
                return lines;
            }
        }
    }

    private Coupon findCoupon(Connection c, String code, BigDecimal subtotal) throws SQLException {
        if (code == null || code.isBlank()) return null;
        String sql = "SELECT coupon_id,discount_type,discount_value,minimum_order,maximum_discount FROM coupons WHERE UPPER(code)=UPPER(?) AND status=1 AND (expiry_date IS NULL OR expiry_date>=CURDATE()) FOR UPDATE";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, code.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new IllegalArgumentException("That coupon code is invalid or expired.");
                BigDecimal minimum = rs.getBigDecimal("minimum_order");
                if (subtotal.compareTo(minimum) < 0) throw new IllegalArgumentException("Your order does not meet this coupon's minimum amount.");
                return new Coupon(rs.getLong("coupon_id"), rs.getString("discount_type"), rs.getBigDecimal("discount_value"), minimum, rs.getBigDecimal("maximum_discount"));
            }
        }
    }

    private BigDecimal calculateDiscount(Coupon coupon, BigDecimal subtotal) {
        if (coupon == null) return BigDecimal.ZERO;
        BigDecimal discount = "PERCENT".equals(coupon.type())
                ? subtotal.multiply(coupon.value()).divide(new BigDecimal("100")) : coupon.value();
        if (coupon.maximum() != null) discount = discount.min(coupon.maximum());
        return discount.min(subtotal).setScale(2, java.math.RoundingMode.HALF_UP);
    }

    private long insertOrder(Connection c, long userId, long addressId, BigDecimal total, BigDecimal discount) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("INSERT INTO orders(user_id,address_id,total_amount,discount,payment_method,payment_status,order_status) VALUES(?,?,?,?, 'MOCK','PAID','PLACED')", Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, userId); ps.setLong(2, addressId); ps.setBigDecimal(3, total); ps.setBigDecimal(4, discount); ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) { if (keys.next()) return keys.getLong(1); }
        }
        throw new SQLException("Could not create the order.");
    }

    private void insertItemsAndReduceStock(Connection c, long orderId, List<Line> lines) throws SQLException {
        try (PreparedStatement item = c.prepareStatement("INSERT INTO order_items(order_id,product_id,product_name,quantity,price) VALUES(?,?,?,?,?)");
             PreparedStatement stock = c.prepareStatement("UPDATE products SET stock=stock-? WHERE product_id=? AND stock>=?")) {
            for (Line line : lines) {
                item.setLong(1, orderId); item.setLong(2, line.productId()); item.setString(3, line.name());
                item.setInt(4, line.quantity()); item.setBigDecimal(5, line.price()); item.addBatch();
                stock.setInt(1, line.quantity()); stock.setLong(2, line.productId()); stock.setInt(3, line.quantity());
                if (stock.executeUpdate() != 1) throw new IllegalStateException("Stock changed while placing your order. Please try again.");
            }
            item.executeBatch();
        }
    }

    private void insertPayment(Connection c, long orderId, BigDecimal total) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("INSERT INTO payments(order_id,payment_method,transaction_reference,amount,payment_status) VALUES(?,'MOCK',? ,?,'SUCCESS')")) {
            ps.setLong(1, orderId); ps.setString(2, "MOCK-" + UUID.randomUUID()); ps.setBigDecimal(3, total); ps.executeUpdate();
        }
    }

    private void insertCouponUsage(Connection c, long couponId, long userId, long orderId) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("INSERT INTO coupon_usage(coupon_id,user_id,order_id) VALUES(?,?,?)")) {
            ps.setLong(1, couponId); ps.setLong(2, userId); ps.setLong(3, orderId); ps.executeUpdate();
        }
    }
}
