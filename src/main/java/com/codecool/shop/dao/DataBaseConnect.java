package com.codecool.shop.dao;

import java.sql.*;

public abstract class DataBaseConnect {
    /**Set up System Environments as the following:
    DB_USER = for username
    DB_NAME = for database name (coffeefilter)
    HOST = for DB address (localhost)
    PASSWORD = for the password to access the pSQL Server **/
    private static final String DB_URL = "jdbc:postgresql://" + System.getenv("HOST") + ":5432/" + System.getenv("DB_NAME");
    private static final String DB_USER = System.getenv("DB_USER");
    private static final String DB_PASSWORD = System.getenv("PASSWORD");

    protected static Connection connection = null;
    protected static Statement statement = null;
    protected static ResultSet resultSet = null;

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DB_URL,
                DB_USER,
                DB_PASSWORD);
    }

    public static void executeQuery(String query) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
        ) {
            statement.execute(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeDataBaseConnection() throws SQLException {
        try {
            this.connection.close();
            this.statement.close();
            this.resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
