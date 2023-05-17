package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
        try (Connection connection = Util.getDbConnection();
             Statement st = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS users (id INT PRIMARY KEY NOT NULL AUTO_INCREMENT, " +
                    "name VARCHAR(255) NOT NULL, lastname VARCHAR(255) NOT NULL, age INT NOT NULL)";
            st.executeUpdate(sql);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
//
    public void dropUsersTable() {
        try (Connection connection = Util.getDbConnection();
             Statement st = connection.createStatement()
        ) {
            String sql = "DROP TABLE IF EXISTS users";
            st.executeUpdate(sql);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String query = "INSERT INTO users (name, lastname, age) " +
                "VALUES (?, ?, ?)";
        try (Connection connection = Util.getDbConnection();
        PreparedStatement stmt = connection.prepareStatement(query)
        ) {
            stmt.setString(1, name);
            stmt.setString(2, lastName);
            stmt.setInt(3, age);
            stmt.executeUpdate();
            System.out.printf("User с именем – %s добавлен в базу данных", name);
            System.out.println();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        String query = "DELETE FROM users WHERE id = ";
        try (Connection connection = Util.getDbConnection();
             PreparedStatement stmt = connection.prepareStatement(query)
        ) {
            stmt.executeUpdate(query + id);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> listUsers = new ArrayList<>();
        try (Connection connection = Util.getDbConnection();
             ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM users");
        ) {
            while (rs.next()) {
                User user = new User(rs.getString("name"),
                        rs.getString("lastname"), rs.getByte("age"));
                user.setId(rs.getLong("id"));
                listUsers.add(user);
                System.out.printf(user.toString());
                System.out.println();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return listUsers;
    }

    public void cleanUsersTable() {
        try (Connection connection = Util.getDbConnection();
             Statement stmt = connection.createStatement()
        ) {
            stmt.executeUpdate("TRUNCATE users");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
