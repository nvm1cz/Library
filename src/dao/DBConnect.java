package dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnect {
    public static Connection connectDB(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/library_db", "root", "123456");
            return connect;
        }catch(Exception e){e.printStackTrace();}
        return null;
    }
}
