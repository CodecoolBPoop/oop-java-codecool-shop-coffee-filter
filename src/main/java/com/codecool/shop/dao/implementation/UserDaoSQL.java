package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.DataBaseConnect;
import com.codecool.shop.dao.UserDao;
import com.codecool.shop.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoSQL extends DataBaseConnect implements UserDao {

    private static UserDaoSQL instance = null;

    public static UserDaoSQL getInstance() {
        if (instance == null) {
            instance = new UserDaoSQL();
        }
        return instance;
    }

    @Override
    public void add(String name, String pswd, String email) {
        String sql = "INSERT INTO users (user_name, password, email)" + "VALUES (?,?,?);";
        try (Connection conn = getDbConnection(); PreparedStatement pstatement = conn.prepareStatement(sql)) {
            pstatement.setString(1, name);
            pstatement.setString(2, pswd);
            pstatement.setString(3, email);

            pstatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Failed to insert into table due to incorrect SQL String!");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Error: JDBC Driver load fail");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean checkNameAndEmail(String name, String email) {
        boolean valid = true;
        String sql = "SELECT user_name, email FROM users";
        try (Connection conn = getDbConnection(); PreparedStatement pstatement = conn.prepareStatement(sql)) {
            try (ResultSet resultSet = pstatement.executeQuery()) {
                while (resultSet.next()) {
                    String retrievedName = resultSet.getString("user_name");
                    String retrievedEmail = resultSet.getString("email");
                    if (retrievedName.equals(name) || retrievedEmail.equals(email)) {
                        valid = false;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to insert into table due to incorrect SQL String!");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Error: JDBC Driver load fail");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return valid;
    }

    @Override
    public boolean checkNameAndPassword(String name, String password) {
        boolean valid = false;
        String sql = "SELECT user_name, password FROM users";
        try (Connection conn = getDbConnection(); PreparedStatement pstatement = conn.prepareStatement(sql)) {
            try (ResultSet resultSet = pstatement.executeQuery()) {
                while (resultSet.next()) {
                    String retrievedName = resultSet.getString("user_name");
                    String retrievedEmail = resultSet.getString("password");
                    if (retrievedName.equals(name) && retrievedEmail.equals(password)) {
                        valid = true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to insert into table due to incorrect SQL String!");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Error: JDBC Driver load fail");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return valid;
    }

    @Override
    public int getUserIdByUsername(String username) {
        String sql = "SELECT id FROM users WHERE user_name = ?";
        try (Connection connection = getDbConnection(); PreparedStatement pstatement = connection.prepareStatement(sql)) {
            pstatement.setString(1, username);
            try (ResultSet resultSet = pstatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
        }
        catch (SQLException e) {
            System.err.println("Failed to insert into table due to incorrect SQL String!");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Error: JDBC Driver load fail");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public User getUserById(int id) {
        String sql = "SELECT id, user_name, email FROM users WHERE id = ?";
        try (Connection connection = getDbConnection(); PreparedStatement pstatement = connection.prepareStatement(sql)) {
            pstatement.setInt(1, id);
            try (ResultSet resultSet = pstatement.executeQuery()) {
                if (resultSet.next()) {
                    int userID = resultSet.getInt("id");
                    String userName = resultSet.getString("user_name");
                    String email = resultSet.getString("email");

                    return new User(userID, userName, email);
                }
            }
        }
        catch (SQLException e) {
            System.err.println("Failed to insert into table due to incorrect SQL String!");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Error: JDBC Driver load fail");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
