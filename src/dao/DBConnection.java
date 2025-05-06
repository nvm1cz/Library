package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// 192.168.110.5
public class DBConnection {
    private static final String url = "jdbc:mysql://localhost:3306/library_db";
    private static final String user = "root";
    private static final String password = "123456";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
