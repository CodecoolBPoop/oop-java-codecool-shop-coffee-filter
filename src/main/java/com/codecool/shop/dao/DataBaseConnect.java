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
    protected static PreparedStatement pstatement = null;
    protected static Statement statement = null;
    protected static ResultSet resultSet = null;


    public Connection getDbConnection() throws ClassNotFoundException, SQLException, Exception{
        try {
            System.out.println("Registering pSQL JDBC driver");
            Class.forName("org.postgresql.Driver");

            System.out.println("Creating a connection to pSQL DB");
            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            return this.connection;

        } catch (ClassNotFoundException e) {
            System.err.println("Error: Unable to load pSQL driver");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.err.println("Error: Unable to execute SQL querry");
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.err.println("Error: Unknown error");
            e.printStackTrace();
            return null;
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
