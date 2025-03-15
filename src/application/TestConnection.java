package application;

import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        Connection connection = database.connectDB();
        if (connection != null) {
            System.out.println("Connection established successfully!");
        } else {
            System.out.println("Failed to establish connection!");
        }
    }
}
