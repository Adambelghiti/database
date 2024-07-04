package com.example.test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDBConnection {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/demo";
        String user = "root";
        String password = "adam";

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connection successful!");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
