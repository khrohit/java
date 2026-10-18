package com.shopsphere.service;

import com.shopsphere.dao.CartDAO;
import com.shopsphere.model.CartItem;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class CartService {
    private final CartDAO cart = new CartDAO();
    public List<CartItem> items(long userId) throws SQLException { return cart.findByUser(userId); }
    public void add(long userId, long productId, int quantity) throws SQLException {
        if (productId <= 0 || quantity < 1 || quantity > 99) throw new IllegalArgumentException("Choose a valid product quantity.");
        cart.add(userId, productId, quantity);
    }
    public void update(long userId, long productId, int quantity) throws SQLException {
        if (productId <= 0 || quantity > 99) throw new IllegalArgumentException("Choose a valid quantity.");
        cart.update(userId, productId, quantity);
    }
    public void remove(long userId, long productId) throws SQLException { cart.remove(userId, productId); }
    public BigDecimal total(List<CartItem> items) {
        return items.stream().map(CartItem::getLineTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
