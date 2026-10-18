package com.shopsphere.service;

import com.shopsphere.dao.OrderDAO;
import com.shopsphere.dao.AddressDAO;
import java.sql.SQLException;

public class CheckoutService {
    private final OrderDAO orders = new OrderDAO();
    private final AddressDAO addresses = new AddressDAO();
    public long placeOrder(long userId, String line, String city, String state, String pincode, String coupon) throws SQLException {
        if (blank(line) || blank(city) || blank(state) || blank(pincode)) throw new IllegalArgumentException("Enter the complete delivery address.");
        if (line.length() > 255 || city.length() > 100 || state.length() > 100 || pincode.length() > 20)
            throw new IllegalArgumentException("One of the delivery address fields is too long.");
        long addressId = addresses.create(userId, line, city, state, pincode);
        return orders.placeOrder(userId, addressId, coupon);
    }
    private boolean blank(String value) { return value == null || value.isBlank(); }
}
