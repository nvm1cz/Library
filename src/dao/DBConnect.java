package dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnect {

    public static Connection connectDB() {
        try {
            // Load driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Connection string using Windows Authentication
            String url = "jdbc:sqlserver://localhost:1433;" +
                         "databaseName=LIB;" +
                         "integratedSecurity=true;" + 
                         "trustServerCertificate=true;";  // <== this uses your Windows account

            return DriverManager.getConnection(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
