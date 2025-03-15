package application;

import java.sql.Connection;
import java.sql.DriverManager;

public class database {
    public static Connection connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/cafe"; 
            String user = "root"; 
            String password = "osamalayan243146#"; 
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

