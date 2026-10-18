package com.shopsphere.util;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class Database {
    private static final String DEFAULT_URL = "jdbc:mysql://127.0.0.1:3306/shopsphere?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";

    private Database() { }

    public static Connection getConnection() throws SQLException {
        try {
            // Load explicitly so the JDBC driver is registered reliably inside Tomcat's webapp classloader.
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException exception) {
            throw new SQLException("MySQL Connector/J is missing from the ShopSphere web application.", exception);
        }
        Properties config = loadConfig();
        String url = value("SHOPSPHERE_DB_URL", "db.url", DEFAULT_URL, config);
        String user = value("SHOPSPHERE_DB_USER", "db.username", "root", config);
        String password = value("SHOPSPHERE_DB_PASSWORD", "db.password", "", config);
        return DriverManager.getConnection(url, user, password);
    }

    private static Properties loadConfig() {
        Properties properties = new Properties();
        String configured = System.getProperty("shopsphere.config");
        String base = System.getProperty("catalina.base");
        Path path = configured != null
                ? Path.of(configured)
                : base != null ? Path.of(base, "conf", "shopsphere-db.properties") : null;
        if (path == null || !Files.isRegularFile(path)) return properties;
        try (InputStream input = Files.newInputStream(path)) {
            properties.load(input);
        } catch (Exception exception) {
            throw new IllegalStateException("Could not read ShopSphere database config at " + path, exception);
        }
        return properties;
    }

    private static String value(String envKey, String propertyKey, String fallback, Properties properties) {
        String env = System.getenv(envKey);
        if (env != null && !env.isBlank()) return env;
        return properties.getProperty(propertyKey, fallback);
    }
}
