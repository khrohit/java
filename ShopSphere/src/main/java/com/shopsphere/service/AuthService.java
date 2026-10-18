package com.shopsphere.service;

import com.shopsphere.dao.UserDAO;
import com.shopsphere.model.User;
import com.shopsphere.util.PasswordUtil;
import java.sql.SQLException;
import java.util.Locale;

public class AuthService {
    private final UserDAO users = new UserDAO();

    public User login(String email, String password) throws SQLException {
        String normalized = email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
        if (normalized.isBlank() || password == null) return null;
        String hash = users.findPasswordHash(normalized);
        return hash != null && PasswordUtil.verify(password, hash) ? users.findByEmail(normalized) : null;
    }

    public void register(String name, String email, String mobile, String password) throws SQLException {
        String cleanName = name == null ? "" : name.trim();
        String cleanEmail = email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
        if (cleanName.length() < 2 || cleanName.length() > 120) throw new IllegalArgumentException("Enter a name between 2 and 120 characters.");
        if (!cleanEmail.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) throw new IllegalArgumentException("Enter a valid email address.");
        if (password == null || password.length() < 8) throw new IllegalArgumentException("Use a password with at least 8 characters.");
        if (mobile != null && mobile.length() > 30) throw new IllegalArgumentException("Phone number is too long.");
        try {
            users.create(cleanName, cleanEmail, mobile == null ? "" : mobile.trim(), PasswordUtil.hash(password));
        } catch (SQLException exception) {
            if ("23000".equals(exception.getSQLState())) throw new IllegalArgumentException("An account with that email already exists.");
            throw exception;
        }
    }
}
