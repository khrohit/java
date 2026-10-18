package com.shopsphere.labs.jndi;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;

public class JndiDataSourceDemo {
    public static void main(String[] args) throws Exception {
        DataSource dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/ShopSphereDB");
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("JNDI data source connected: " + !connection.isClosed());
        }
    }
}
