package com.shopsphere.dao;

import com.shopsphere.util.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddressDAO {
    public long create(long userId, String line, String city, String state, String pincode) throws SQLException {
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(
                "INSERT INTO addresses(user_id,address_line,city,state,pincode,address_type) VALUES(?,?,?,?,?,'SHIPPING')", PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, userId); ps.setString(2, line); ps.setString(3, city); ps.setString(4, state); ps.setString(5, pincode);
            ps.executeUpdate();
            try (var keys = ps.getGeneratedKeys()) { if (keys.next()) return keys.getLong(1); }
        }
        throw new SQLException("Could not save the delivery address.");
    }
}
