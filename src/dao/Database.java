package dao;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author MarcoMan
 * Channel: https://www.youtube.com/channel/UCPgcmw0LXToDn49akUEJBkQ
 * Please support our channel (SUBSCRIBE --> TURN ON NOTIFICATION --> HIT THE LIKE BUTTON)
 */
public class Database {
    
    public static Connection connectDB(){
        try{
        Class.forName("com.mysql.cj.jdbc.Driver");
//        MY DATABASE IS marcoData
        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/library_db", "root", "123456");
            return connect;
        }catch(Exception e){e.printStackTrace();}
        
        return null;
    }
    
}
