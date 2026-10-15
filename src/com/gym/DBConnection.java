package com.gym;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/gym_management";

    private static final String USER = "root";

    private static final String PASSWORD = "root@123";

    public static Connection getConnection() {

        try {
            Connection con = DriverManager.getConnection(
                    URL,
                    USER,
                    PASSWORD);

            System.out.println("================================");
            System.out.println("DATABASE CONNECTED SUCCESSFULLY!");
            System.out.println("================================");

            return con;

        } catch (SQLException e) {

            System.out.println("DATABASE CONNECTION FAILED!");
            e.printStackTrace();

            return null;
        }
    }
}